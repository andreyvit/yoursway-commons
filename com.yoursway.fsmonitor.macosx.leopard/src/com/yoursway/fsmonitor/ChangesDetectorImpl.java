package com.yoursway.fsmonitor;

import static com.yoursway.utils.YsCollections.addIfNotNull;
import static com.yoursway.utils.YsPathUtils.isChildOrParent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.yoursway.fsmonitor.spi.ChangesDetector;
import com.yoursway.fsmonitor.spi.ChangesListener;
import com.yoursway.fsmonitor.spi.MonitoringRequest;
import com.yoursway.utils.YsPathUtils;
import com.yoursway.utils.annotations.SynchronizedWithMonitorOfField;
import com.yoursway.utils.annotations.UseFromCarbonRunLoopThread;
import com.yoursway.utils.annotations.UsedFromJNI;

public class ChangesDetectorImpl implements ChangesDetector {
    
    static {
        System.loadLibrary("ys-fs-monitor-macosx_leopard");
    }
    
    private native void initializeNatives();
    
    private native boolean queueSafeReschedulingRequest();
    
    private native long FSEventStreamCreate(String[] paths, long sinceWhen, double latency);
    
    private native void FSEventStreamScheduleWithRunLoop(long streamId);
    
    private native boolean FSEventStreamStart(long streamId);
    
    private native void FSEventStreamStop(long streamId);
    
    private native void FSEventStreamInvalidate(long streamId);
    
    private native void FSEventStreamRelease(long streamId);
    
    private native static void CFRunLoopRun();
    
    class Request implements MonitoringRequest {
        
        private final String monitoredPath;
        private final ChangesListener listener;
        
        public Request(File directory, ChangesListener listener) {
            if (directory == null)
                throw new NullPointerException("directory is null");
            if (listener == null)
                throw new NullPointerException("listener is null");
            try {
                directory = directory.getCanonicalFile();
            } catch (IOException e) {
                directory = directory.getAbsoluteFile();
            }
            String directoryPath = directory.getPath();
            directoryPath = YsPathUtils.removeTrailingSeparator(directoryPath);
            this.monitoredPath = directoryPath;
            this.listener = listener;
        }
        
        public String path() {
            return monitoredPath;
        }
        
        public void addNotifiersTo(Collection<Runnable> notifiers, String[] paths) {
            String monitoredPath = this.monitoredPath;
            for (final String path : paths)
                if (isChildOrParent(monitoredPath, path))
                    notifiers.add(new Runnable() {

                        public void run() {
                            listener.pathChanged(path);
                        }
                        
                    });
        }
       
        public void dispose() {
            Collection<String> paths;
            long changeId;
            synchronized (activeRequests) {
                activeRequests.remove(this);
                paths = calculateActivePaths();
                changeId = activeRequestsChangeCount++;
            }
            schedule(paths, changeId);
        }
        
    }
    
    @UseFromCarbonRunLoopThread
    private long lastSeenEventId = -1;
    
    @SuppressWarnings("unused")
    @UsedFromJNI
    private long runLoopHandle = 0;
    
    /**
     * A collection of currently active (non-disposed) monitoring requests.
     */
    @SynchronizedWithMonitorOfField("activeRequests")
    private Collection<Request> activeRequests = new HashSet<Request>();
    
    @SynchronizedWithMonitorOfField("activeRequests")
    private long activeRequestsChangeCount = 0;
    
    /**
     * In <code>ACTIVE</code> state, the paths that are currently being
     * monitored by FSEventStream.
     * 
     * In <code>RESCHEDULING</code> state, the paths that will be monitored by
     * FSEventStream once the rescheduling is complete.
     */
    @SynchronizedWithMonitorOfField("stateLock")
    private Collection<String> currentlyMonitoredPaths = new HashSet<String>();
    
    /**
     * Used to prevent later changes from being lost due to a race condition
     * that might occur because we are using two separate locks.
     */
    @SynchronizedWithMonitorOfField("stateLock")
    private long lastScheduledChangedId = -1;
    
    @SynchronizedWithMonitorOfField("stateLock")
    private long streamId;
    
    @SynchronizedWithMonitorOfField("stateLock")
    private State state = State.INACTIVE;
    
    @SynchronizedWithMonitorOfField("stateLock")
    private boolean isScheduled = false;
    
    private Object stateLock = new Object();
    
    public ChangesDetectorImpl() {
        initializeNatives();
    }
    
    public MonitoringRequest monitor(File directory, ChangesListener listener) {
        Request newRequest = new Request(directory, listener);
        Collection<String> paths;
        long changeId;
        synchronized (activeRequests) {
            activeRequests.add(newRequest);
            paths = calculateActivePaths();
            changeId = activeRequestsChangeCount++;
        }
        schedule(paths, changeId);
        return newRequest;
    }
    
    private Collection<String> calculateActivePaths() {
        Collection<String> paths;
        paths = new HashSet<String>(activeRequests.size());
        for (Request request : activeRequests)
            addIfNotNull(paths, request.path());
        return paths;
    }
    
    void schedule(Collection<String> newPaths, long changeId) {
        synchronized (stateLock) {
            if (!state.canChangeToAnotherState())
                return;
            if (changeId < lastScheduledChangedId)
                return; // prevent later changes from being lost
            lastScheduledChangedId = changeId;
            if (newPaths.equals(currentlyMonitoredPaths))
                return;
            currentlyMonitoredPaths = newPaths;
            if (!state.shouldInitiateRescheduling())
                return;
            state = State.RESCHEDULING;
        }
        scheduleRestart();
    }

    private void scheduleRestart() {
        if (!queueSafeReschedulingRequest())
            handleSafeToReschedule();
    }
    
    @UsedFromJNI
    void handleChange(String[] paths, long[] eventIds) {
//        System.out.println("CHANGE!");
//        for (int i = 0; i < eventIds.length; i++) {
//            String path = paths[i];
//            long id = eventIds[i];
//            if (id > lastSeenEventId)
//                lastSeenEventId = id;
//            System.out.println(" #" + id + " - " + path);
//        }
//        System.out.flush();
        
        for (int i = 0; i < paths.length; i++)
            paths[i] = removeTrailingSeparator(paths[i]);
        
        // minimize the time we spend inside the synchronized area
        Collection<Runnable> notifiers = new ArrayList<Runnable>();
        synchronized (activeRequests) {
            for (Request request : activeRequests)
                request.addNotifiersTo(notifiers, paths);
        }
        for (Runnable runnable : notifiers)
            runnable.run();
    }
    
    @UsedFromJNI
    void handleSafeToReschedule() {
        synchronized (state) {
            if (isScheduled) {
                FSEventStreamStop(streamId);
//                System.out.println("FSEventStream stopped.");
                FSEventStreamInvalidate(streamId);
//                System.out.println("FSEventStream invalidated.");
                FSEventStreamRelease(streamId);
                System.out.println("FSEventStream disposed.");
                isScheduled = false;
            }
            if (currentlyMonitoredPaths.isEmpty() || !state.canActivate()) {
                if (state.canChangeToAnotherState())
                    state = State.INACTIVE;
            } else {
                String[] paths = currentlyMonitoredPaths.toArray(new String[currentlyMonitoredPaths.size()]);
                streamId = FSEventStreamCreate(paths, lastSeenEventId, 1.0);
                if (streamId == 0)
                    throw new RuntimeException("Cannot create FSEventStream");
//                System.out.println("FSEventStream created.");
                FSEventStreamScheduleWithRunLoop(streamId);
//                System.out.println("FSEventStream scheduled.");
                if (!FSEventStreamStart(streamId))
                    throw new RuntimeException("Cannot start FSEventStream");
                System.out.println("FSEventStream running...");
                isScheduled = true;
                if (state.canChangeToAnotherState())
                    state = State.ACTIVE;
            }
        }
    }
    
    public static void main(String[] args) {
        ChangesDetectorImpl detector = new ChangesDetectorImpl();
        detector.monitor(new File("/Users/andreyvit"), new ChangesListener() {

            public void pathChanged(String path) {
                System.out.println("Changed: " + path);
            }
            
        });
        CFRunLoopRun();
        detector.dispose();
    }
    
    public void dispose() {
        synchronized(state) {
            state = State.DISPOSED;
        }
        scheduleRestart();
    }

    /**
     * @deprecated Use {@link YsPathUtils#removeTrailingSeparator(String)} instead
     */
    public static String removeTrailingSeparator(String directoryPath) {
        return YsPathUtils.removeTrailingSeparator(directoryPath);
    }
    
}
