package com.yoursway.completion.gui;

import com.yoursway.completion.gui.CompletionProvider.DisplayState;

public class CompletionStrategy{
	enum TabState{
		NOT_PRESSED,
		SHORT_PRESS,
		LONG_PRESS
	};

	private boolean calculationInProgress; // no 
	private TabState tabPressed = TabState.NOT_PRESSED;
	private CompletionProvider requestor;

	public CompletionStrategy(CompletionProvider provider) {
		this.requestor = provider;
	}
	
	private void cancel() {
		calculationInProgress = false;
		if(tabPressed != TabState.NOT_PRESSED){
			tabPressed = TabState.NOT_PRESSED;
			updateDisplay();
		}
	}
	
	private void complete() {
		if(tabPressed != TabState.NOT_PRESSED){
			tabPressed = TabState.NOT_PRESSED;
			updateDisplay();
			requestor.complete();
		}
	}
	
	void focusLost(){
		cancel();
	}

	void tabPressed(){
		if(tabPressed == TabState.NOT_PRESSED){
			tabPressed = TabState.SHORT_PRESS;
			calculationInProgress = true;
			requestor.start();
			updateDisplay();
		}
	}
	
	void tabReleased(){
		if(calculationInProgress) {
			cancel();
		} else {
			complete();
		}
	}
	
	void keyPressed(){
		requestor.restart();
		updateDisplay();
	}
	
	private void updateDisplay() {
		if(tabPressed == TabState.NOT_PRESSED){
			requestor.show(DisplayState.NOTHING);
		}else if(calculationInProgress){
			requestor.show(DisplayState.IN_PROGRESS);
		}else if(tabPressed == TabState.SHORT_PRESS){
			requestor.show(DisplayState.SUGGESTION);
		}else if(tabPressed == TabState.LONG_PRESS){
			requestor.show(DisplayState.LIST);
		}
	}
	
	void tabLongClickThreshold(){
		if(tabPressed == TabState.SHORT_PRESS){
			tabPressed = TabState.LONG_PRESS;
			updateDisplay();
		}
	}
	
	void proposalsCalculated(){
		calculationInProgress = false;
		updateDisplay();
	}
}
