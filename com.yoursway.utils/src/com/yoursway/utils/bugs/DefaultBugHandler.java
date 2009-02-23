package com.yoursway.utils.bugs;

public class DefaultBugHandler implements BugHandler {

	public void bug(Throwable throwable) {
		throwable.printStackTrace(System.err);
	}
    
}
