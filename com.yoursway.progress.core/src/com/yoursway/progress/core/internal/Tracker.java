package com.yoursway.progress.core.internal;

import com.yoursway.progress.core.Naming;

public interface Tracker {
    
    void childStarting(int index);

    void checkCancellation();

    void incrementWorkBecauseOfChild(double delta);

    void skipWorkBecauseOfChild(double delta);
    
    void setTaskNameFromChild(String name, Naming naming);
    
    void setTaskNameFromChild(String name, int level);
    
}
