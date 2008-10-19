package com.yoursway.completion.gui;

public interface CompletionProvider {
	enum DisplayState{
		NOTHING,
		SUGGESTION,
		LIST,
		IN_PROGRESS
	}
	
	public void start();
	public void restart();
	public void complete();
	public void cancel();
	
	public void show(DisplayState state);
}
