package com.yoursway.utils.bugs;

import static com.yoursway.utils.bugs.Severity.UNKNOWN;

public class Bugs {
    
    private static BugHandler handler = new DefaultBugHandler();
    
    public static void setHandler(BugHandler handler) {
        Bugs.handler = handler;
    }
    
    public static void listenerFailed(Throwable error, Object listener, String event) {
        handler.error(UNKNOWN, error, event);
    }
    
    public static void listenerFailed(Throwable error, Object listener) {
        listenerFailed(error, listener, null);
    }
    
    public static void illegalCaseRecovery(Severity severity, String description) {
        handler.error(severity, null, description);
    }
    
    public static void unknownErrorRecovery(Severity severity, Throwable error) {
        handler.error(severity, error, "unexpected error");
    }
    
}
