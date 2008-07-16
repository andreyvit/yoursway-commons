package com.yoursway.fsmonitor;

import java.io.File;

import com.yoursway.fsmonitor.spi.ChangesListener;
import com.yoursway.fsmonitor.spi.MonitoringRequest;

public class FileSystemMonitor {
    
    private final File directory;
    
    private ChangesListener listenerImpl = new ChangesListener() {

        public void pathChanged(String path) {
            listener.changed(path);
        }
        
    };

    private MonitoringRequest request;

    private final FileSystemChangesListener listener;

    public FileSystemMonitor(FileSystemMonitoringContext context, File directory, FileSystemChangesListener listener) {
        if (context == null)
            throw new NullPointerException("context is null");
        if (directory == null)
            throw new NullPointerException("file is null");
        if (listener == null)
            throw new NullPointerException("listener is null");
        this.directory = directory;
        this.listener = listener;
        request = context.add(directory, listenerImpl);
    }
    
    public File directory() {
        return directory;
    }
    
    public void dispose() {
        request.dispose();
    }
    
}
