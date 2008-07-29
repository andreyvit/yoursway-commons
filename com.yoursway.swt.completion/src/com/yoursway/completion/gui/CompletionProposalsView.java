/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.yoursway.completion.gui;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class CompletionProposalsView {
	Shell shell;
	List list;
	int minimumWidth;
	Thread thread;

	public CompletionProposalsView(Shell parent) {
		this(parent, 0);
	}

	public CompletionProposalsView(Shell parent, int style) {
		shell = new Shell(parent, checkStyle(style) | SWT.ON_TOP);

		list = new List(shell, SWT.SINGLE | SWT.V_SCROLL);

		// resize shell when list resizes
		shell.addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e) {
			}

			public void controlResized(ControlEvent e) {
				Rectangle shellSize = shell.getClientArea();
				list.setSize(shellSize.width, shellSize.height);
			}
		});

		// return list selection on Mouse Up or Carriage Return
		list.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent e) {
			}

			public void mouseDown(MouseEvent e) {
			}

			public void mouseUp(MouseEvent e) {
				shell.setVisible(false);
			}
		});
		list.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				if (e.character == '\r') {
					shell.setVisible(false);
				}
			}
		});
	}

	synchronized public int getCurrentSelectionIndex() {
		return list.getSelectionIndex();
	}

	private static int checkStyle(int style) {
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		return style & mask;
	}

	synchronized public String[] getItems() {
		return list.getItems();
	}

	synchronized public int getMinimumWidth() {
		return minimumWidth;
	}

	synchronized private void onHide() {
		if (shell.isDisposed()) {
			return;
		}
	}

	synchronized public void show(Rectangle rect, Control focusedControl) {
		shell.setBounds(rect);
		list.setBounds(0, 0, shell.getBounds().width, shell.getBounds().height);
		shell.open();
		// list.setFocus();
		focusedControl.setFocus();

		final Display display = shell.getDisplay();

		thread = new Thread(new Runnable() {

			public void run() {
				synchronized (CompletionProposalsView.this) {
					try {
						while (!shell.isDisposed() && shell.isVisible()) {
							if (!display.readAndDispatch())
								display.sleep();
						}
						onHide();
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.run();
	}

	synchronized public void setLocation(Point location) {
		shell.setLocation(location);
	}

	synchronized public void hide() {
		shell.setVisible(false);
	}

	synchronized public void setItems(String[] strings) {
		list.setItems(strings);
		list.setSelection(0);
	}

	synchronized public void setMinimumWidth(int width) {
		if (width < 0)
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);

		minimumWidth = width;
	}

	synchronized public boolean isVisible() {
		return shell.isVisible();
	}
}
