package com.yoursway.utils.bugs;

public interface BugHandler {

    void error(Severity severity, Throwable error, String details);
    
}
