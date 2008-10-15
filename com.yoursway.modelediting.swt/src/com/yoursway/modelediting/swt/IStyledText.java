package com.yoursway.modelediting.swt;

import org.eclipse.swt.widgets.Listener;

public interface IStyledText {

	void addListener(int eventType, Listener listener);

	void removeListener(int eventType, Listener listener);

	void setText(String text);

	void replaceTextRange(int start, int length, String text);
	
	String getText();

}
