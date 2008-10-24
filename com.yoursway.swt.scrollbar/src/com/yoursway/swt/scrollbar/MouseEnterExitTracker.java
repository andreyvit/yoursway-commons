package com.yoursway.swt.scrollbar;

import static com.yoursway.swt.scrollbar.CompositeUtils.addAllChildrenListener;
import static com.yoursway.swt.scrollbar.CompositeUtils.removeAllChildrenListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class MouseEnterExitTracker {

	private int mouseEnterLevel = 0;
	private Listener trackingListener;
	private final Composite control;
	
	public MouseEnterExitTracker(Composite control, final Listener listener) {
		this.control = control;
		trackingListener = new Listener() {

			public void handleEvent(Event event) {
				if (event.type == SWT.MouseEnter) {
					if (mouseEnterLevel == 0)
						listener.handleEvent(event);
					mouseEnterLevel++;
				} else if (event.type == SWT.MouseExit) {
					if (mouseEnterLevel == 1)
						listener.handleEvent(event);
					mouseEnterLevel--;
				}
			}

		};
		addAllChildrenListener(control, SWT.MouseEnter, trackingListener);
		addAllChildrenListener(control, SWT.MouseExit, trackingListener);
	}
	
	public void uninstall() {
		removeAllChildrenListener(control, SWT.MouseEnter, trackingListener);
		removeAllChildrenListener(control, SWT.MouseExit, trackingListener);
	}
		
}
