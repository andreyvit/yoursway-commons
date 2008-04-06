package com.yoursway.progress.core;

import com.yoursway.progress.core.internal.RootProgressTracker;

public class ProgressTracking {
    
    public static Progress track(ProgressReporter reporter) {
        return new RootProgressTracker(reporter).createProgress();
    }
    
}
