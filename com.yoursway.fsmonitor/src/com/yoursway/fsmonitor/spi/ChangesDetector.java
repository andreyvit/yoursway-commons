package com.yoursway.fsmonitor.spi;

import java.io.File;

public interface ChangesDetector {
    
    MonitoringRequest monitor(File directory, ChangesListener listener);

    void dispose();
    
}
