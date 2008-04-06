package com.yoursway.pingback.core;

public class Pingback {
    
    private static volatile boolean failFastMode = false;

    public static void enterFailFastMode() {
        failFastMode = true;
    }
    
    public static void ignorableErrorCondition(String explanation) {
        AssertionError exception = new AssertionError(explanation);
        if (failFastMode)
            throw exception;
        exception.printStackTrace(System.err);
    }
    
    public static void ignorableStateError(String explanation) {
        IllegalStateException exception = new IllegalStateException(explanation);
        if (failFastMode)
            throw exception;
        exception.printStackTrace(System.err);
    }
    
}
