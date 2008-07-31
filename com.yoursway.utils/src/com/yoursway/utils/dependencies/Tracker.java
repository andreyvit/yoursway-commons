package com.yoursway.utils.dependencies;

import java.util.ArrayList;
import java.util.List;

import com.yoursway.utils.AutoThreadLocal;
import com.yoursway.utils.bugs.Bugs;

public class Tracker {
    
    private static AutoThreadLocal<DependeesRequestor> slot = AutoThreadLocal.create();
    
    private static ThreadLocal<List<TrackingSectionListener>> listenerSlot = new ThreadLocal<List<TrackingSectionListener>>() {
        protected List<TrackingSectionListener> initialValue() {
            return new ArrayList<TrackingSectionListener>();
        }
    };
    
    public static void runAndTrack(Runnable runnable, DependeesRequestor requestor) {
        if (requestor == null)
            throw new NullPointerException("requestor is null");
        
        List<TrackingSectionListener> list = listenerSlot.get();
        int oldSize = list.size();
        
        slot.runWith(runnable, requestor);
        
        notifyFinishListeners(list, oldSize);
    }

    private static void notifyFinishListeners(List<TrackingSectionListener> list, int oldSize) {
        int newSize = list.size();
        if (newSize > oldSize) {
            for (int i = oldSize; i < newSize; i++) {
                TrackingSectionListener listener = list.get(i);
                try {
                    listener.trackingFinished();
                } catch (Throwable e) {
                    Bugs.listenerFailed(e, listener);
                }
            }
            while(list.size() > oldSize)
                list.remove(list.size() - 1);
        }
    }
    
    public static void dependsOn(Dependee dependee) {
        DependeesRequestor requestor = slot.get();
        if (requestor != null)
            requestor.dependsOn(dependee);
    }
    
    public static void addFinishListener(TrackingSectionListener listener) {
        if (listener == null)
            throw new NullPointerException("listener is null");
        if (slot.get() == null)
            throw new IllegalStateException("Cannot add a 'tracking finished' listener without an active tracking section");
        listenerSlot.get().add(listener);
    }
    
}
