package com.yoursway.fsmonitor;

public interface FileSystemChangesListener {
    
    void operational();
    
    void inoperational();
    
    void changed(String path);
    
}
