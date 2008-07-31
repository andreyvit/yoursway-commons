package com.yoursway.utils.dependencies;

public class NopDependeesRequestor implements DependeesRequestor {
    
    public static final NopDependeesRequestor INSTANCE = new NopDependeesRequestor();
    
    private NopDependeesRequestor() {
    }

    public void dependsOn(Dependee dependee) {
        // nop
    }
    
}
