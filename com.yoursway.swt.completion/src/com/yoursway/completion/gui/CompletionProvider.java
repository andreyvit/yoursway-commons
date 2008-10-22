package com.yoursway.completion.gui;

public interface CompletionProvider {
	enum DisplayState{
		NOTHING,
		SUGGESTION,
		LIST,
		IN_PROGRESS
	}
	
	/**
	 * Called when completion mode initialized
	 */
	public void start();
	
	/**
	 * Called on rebuild proposal list (say, added a key or erased) 
	 */
	public void restart();
	
	/**
	 * Called on successful completion
	 */
	public void complete();
	
	/**
	 * Called on unsuccessful completion 
	 */
	public void cancel();
	
	public void show(DisplayState state);
}
