package com.yoursway.progress.core;

public interface ProgressReporter {
    
    boolean isCancelled();
    
    void setAction(int level, String action);
    
    void setProgress(double progress);
    
}
