package com.yoursway.progress.core.internal;

public interface Tracker {
    
    void childStarting(int index);

    void checkCancellation();

    void incrementWorkBecauseOfChild(double delta);

    void skipWorkBecauseOfChild(double delta);
    
}
