package com.yoursway.fsmonitor.spi;

public interface ChangesListener {
    
    void pathChanged(String path);
    
}
