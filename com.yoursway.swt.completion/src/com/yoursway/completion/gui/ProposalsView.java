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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import com.yoursway.completion.gui.CompletionProvider.DisplayState;

public class ProposalsView{
	private static final String[] NO_PROPOSALS = new String[]{"Sorry, no proposals"};
	private static final String[] IN_PROGRESS = new String[]{"In progress..."};
	Shell shell;
	List list;
	int minimumWidth;
	Thread thread;
	private final CompletionStrategy strategy;
	private String[] completionItems;
	private final Control control;
	private KeyAdapter simpleKeyListener;
	private VerifyKeyListener verifyKeyListener;

	public ProposalsView(Control control, CompletionStrategy strategy) {
		this(control, 0, strategy);
	}

	public ProposalsView(final Control control, int style, CompletionStrategy strategy) {
		this.control = control;
		this.strategy = strategy;
		shell = new Shell(control.getShell(), checkStyle(style) | SWT.ON_TOP | SWT.NO_FOCUS);

		list = new List(shell, SWT.SINGLE | SWT.V_SCROLL);
		
		
		// resize shell when list resizes
		shell.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle shellSize = shell.getClientArea();
				list.setSize(shellSize.width, shellSize.height);
			}
		});
		
		// return list selection on Mouse Up or Carriage Return
		list.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				ProposalsView.this.strategy.tabReleased();
			}
		});
		
		list.addFocusListener(new FocusAdapter(){
			@Override
			public void focusGained(FocusEvent e) {
				control.setFocus();
				ProposalsView.this.strategy.tabReleased();
			}
		});
		simpleKeyListener = new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent event) {
				parentKeyPressed(event);
			}
		};
//		verifyKeyListener = new VerifyKeyListener(){
//			public void verifyKey(VerifyEvent event) {
//				parentKeyPressed(event);
//			}
//		};
	}
	
	public void hookArrowKeys() {
		control.addKeyListener(simpleKeyListener);
//		if(control instanceof StyledText){
//			((StyledText)control).addVerifyKeyListener(verifyKeyListener);
//		}
	}
	
	public void unhookArrowKeys() {
		if(!control.isDisposed()){
			control.removeKeyListener(simpleKeyListener);
//			if(control instanceof StyledText){
//				((StyledText)control).removeVerifyKeyListener(verifyKeyListener);
//			}
		}
	}
	
	void dispose(){
		unhookArrowKeys();
	}
	
	public String[] getItems() {
		return completionItems;
	}

	public int getItemCount() {
		return completionItems.length;
	}

	public int getSelectionIndex() {
		if(completionItems.length == 0) return -1;
		return list.getSelectionIndex();
	}

	private static int checkStyle(int style) {
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		return style & mask;
	}

	public int getMinimumWidth() {
		return minimumWidth;
	}

	public void setItems(String[] strings) {
		list.setItems(strings);
		completionItems = strings;
		if(strings == IN_PROGRESS){
			completionItems = new String[]{};
			list.deselectAll();
		}else if(strings.length == 0){
			list.setItems(NO_PROPOSALS);
			list.deselectAll();
		}else{
			list.setSelection(0);
		}
	}

	public void setMinimumWidth(int width) {
		if (width < 0)
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);

		minimumWidth = width;
	}

	public boolean isVisible() {
		return shell.isVisible();
	}

	public void show(DisplayState state) {
		if(state == DisplayState.NOTHING){
			setVisible(false);
		}else if(state == DisplayState.IN_PROGRESS){
			list.setItems(IN_PROGRESS);
			list.setEnabled(false);
			setVisible(true);
		}else if(state == DisplayState.SUGGESTION){
			setVisible(true);
			list.setEnabled(false);
		}else if(state == DisplayState.LIST){
			setVisible(true);
			list.setEnabled(completionItems.length > 0);
			list.setSelection(completionItems.length>0 ? 0 : -1);
		}
	}

	private void setVisible(boolean visible) {
		if(shell.isVisible() != visible){
			shell.setVisible(visible);
		}
	}

	public void setSize(Point size) {
		shell.setSize(size.x, size.y);
		list.setBounds(0, 0, size.x, size.y);
	}

	public void setLocation(Point location) {
		shell.setLocation(location.x-5, location.y);
	}

	private void traverseToNextItem() {
		if(getSelectionIndex() < getItemCount()-1)
			list.setSelection(getSelectionIndex()+1);
	}

	private void traverseToPrevItem() {
		if(getSelectionIndex() > 0)
			list.setSelection(getSelectionIndex()-1);
	}

	private void parentKeyPressed(KeyEvent e) {
		if(e.keyCode == SWT.ARROW_DOWN){
			traverseToNextItem();
			e.doit = false;
		} else if(e.character == SWT.CR){
			ProposalsView.this.strategy.tabReleased();
			e.doit = false;
		}else if(e.keyCode == SWT.ARROW_UP){
			traverseToPrevItem();
			e.doit = false;
		}
	}
}
