package com.yoursway.utils.dependencies;

import com.yoursway.utils.annotations.CallFromAnyThread_NonReentrant;

public interface DependeesRequestor {
    
    @CallFromAnyThread_NonReentrant
    void dependsOn(Dependee dependee);
    
}
