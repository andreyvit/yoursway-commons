package com.yoursway.utils.dependencies;

import com.yoursway.utils.annotations.CallFromAnyThread_NonReentrant;

public interface Dependee {
    
    @CallFromAnyThread_NonReentrant
    void addListener(DependeeListener listener);
    
    @CallFromAnyThread_NonReentrant
    void removeListener(DependeeListener listener);
    
}
