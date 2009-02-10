package com.yoursway.commons.dependencies;

/**
 * An object should be declared as observable if either (a) it has mutable
 * (non-final) fields or (b) some fields reference mutable objects that are not
 * observables themselves.
 */
public interface Observable {
	
	public void subscribe(Observer listener);
	
	public void unsubscribe(Observer listener);
	
}
