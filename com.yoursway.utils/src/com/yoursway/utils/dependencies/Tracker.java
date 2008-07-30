package com.yoursway.utils.dependencies;

import com.yoursway.utils.AutoThreadLocal;

public class Tracker {
    
    private static AutoThreadLocal<DependeesRequestor> slot = AutoThreadLocal.create();
    
    public static void runAndTrack(Runnable runnable, DependeesRequestor requestor) {
        if (requestor == null)
            throw new NullPointerException("requestor is null");
        slot.runWith(runnable, requestor);
    }
    
    public static void dependsOn(Dependee dependee) {
        DependeesRequestor requestor = slot.get();
        if (requestor != null)
            requestor.dependsOn(dependee);
    }
    
}
