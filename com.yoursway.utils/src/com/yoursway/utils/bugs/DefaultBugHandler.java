package com.yoursway.utils.bugs;

public class DefaultBugHandler implements BugHandler {

    public void error(Severity severity, Throwable error, String details) {
        System.out.print("[" + severity + "] ");
        if (details != null)
            System.out.println(details);
        else
            System.out.println();
        if (error != null)
            error.printStackTrace(System.err);
    }
    
}
