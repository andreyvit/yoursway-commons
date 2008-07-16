package com.yoursway.fsmonitor;

import java.io.File;

import com.yoursway.fsmonitor.spi.ChangesDetector;
import com.yoursway.fsmonitor.spi.ChangesListener;
import com.yoursway.fsmonitor.spi.MonitoringRequest;

public class FileSystemMonitoringContext {
    
    private ChangesDetector detector;

    public FileSystemMonitoringContext() {
        detector = createDetector();
    }
    
    public void dispose() {
        if (detector != null)
            detector.dispose();
    }
    
    @SuppressWarnings("unchecked")
    private ChangesDetector createDetector() {
        try {
            Class<ChangesDetector> detector = (Class<ChangesDetector>) Class
                    .forName("com.yoursway.fsmonitor.ChangesDetectorImpl");
            return detector.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void run() {
    }

    MonitoringRequest add(File directory, ChangesListener listener) {
        return detector.monitor(directory, listener);
    }
    
}
