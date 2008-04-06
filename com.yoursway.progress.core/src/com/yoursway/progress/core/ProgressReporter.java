package com.yoursway.progress.core;

public interface ProgressReporter {
    
    boolean isCancelled();
    
    void setAction(String action);
    
    void setProgress(double progress);
    
}
