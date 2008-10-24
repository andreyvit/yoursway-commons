package com.yoursway.swt.scrollbar;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;

public class CompositeUtils {

	public static void addAllChildrenListener(Composite control, int eventType, Listener listener) {
		Control[] children = control.getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].addListener(eventType, listener);
		}
		control.addListener(eventType, listener);
	}

	public static void removeAllChildrenListener(Composite control, int eventType, Listener listener) {
		Control[] children = control.getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].removeListener(eventType, listener);
		}
		control.removeListener(eventType, listener);
	}
	
}
