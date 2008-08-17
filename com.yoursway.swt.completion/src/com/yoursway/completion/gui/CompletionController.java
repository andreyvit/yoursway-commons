package com.yoursway.completion.gui;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.yoursway.completion.CompletionProposal;
import com.yoursway.completion.CompletionProposalUpdatesListener;
import com.yoursway.completion.CompletionProposalsProvider;
import com.yoursway.completion.gui.internal.CompletionControllerState;
import com.yoursway.document.Document;

public class CompletionController implements CompletionProposalUpdatesListener {
	protected static final long TIME_TO_HOLD_TAB_BEFORE_POPUP = 1000;
	private CompletionProposalsView list;
	private final StyledText text;
	private List<? extends CompletionProposal> proposals;
	private final Document document;

	private CompletionControllerState currentState;
	
	private CompletionControllerState tabReleased;
	private CompletionControllerState tabHeld;
	private final CompletionProposalsProvider proposalsProvider;
		
	private class TabDownState implements CompletionControllerState{
		private long timestamp;
		public TabDownState() {
			timestamp = System.currentTimeMillis();
		}
		public void onKeyDown() {
			if (System.currentTimeMillis() - timestamp >= TIME_TO_HOLD_TAB_BEFORE_POPUP) {
				if(list.getItems().length > 0 && !list.isVisible()) {
					Point listLocation = completionLocation();
					list.show(new Rectangle(listLocation.x, listLocation.y, 200, 100),text);
				}
				currentState = tabHeld;
			}
		}
		
		public void onKeyUp() {
			if (list.getItems().length > 0 && list.getCurrentSelectionIndex() >= 0) {
				complete();
			}
			currentState = tabReleased;
		}
	};
	private void initStates() {
		tabReleased = new CompletionControllerState(){
			
			public void onKeyDown() {
				proposalsProvider.startCompletionFor(CompletionController.this, text.getText(), text.getCaretOffset());
				currentState = new TabDownState();
			}
			
			public void onKeyUp() {
				//do nothing
			}
		};
		tabHeld = new CompletionControllerState(){
			
			public void onKeyDown() {
				// do nothing
			}
			
			public void onKeyUp() {
				if (list.getItems().length > 0 && list.getCurrentSelectionIndex() >= 0) {
					complete();
				}
				list.hide();
				currentState = tabReleased;
			}
		};
	}
	
	/**
	 * 
	 * @param parent
	 *            parent shell for the completion list.
	 * @param text
	 *            text editor to enable completion for.
	 */
	public CompletionController(final StyledText text, Document document, final CompletionProposalsProvider proposalsProvider) {
		if (text == null || document == null || proposalsProvider == null)
			throw new IllegalArgumentException();

		this.document = document;
		this.text = text;
		this.list = new CompletionProposalsView(text.getShell());
		this.proposalsProvider = proposalsProvider;
		
		initStates();
		currentState = tabReleased;

		final Listener oldKeyDownListener = text.getListeners(SWT.KeyDown)[0];
		text.removeListener(SWT.KeyDown, oldKeyDownListener); 
		text.addListener(SWT.KeyDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.character == SWT.TAB) {
					currentState.onKeyDown();
					event.doit = false;
				} else {
					oldKeyDownListener.handleEvent(event);
					Point listLocation = completionLocation();
					proposalsProvider.startCompletionFor(CompletionController.this, text.getText(), text.getCaretOffset());
					list.setLocation(listLocation);
				}
			}
		});
		text.addListener(SWT.KeyUp, new Listener() {

			public void handleEvent(Event event) {
				list.setLocation(completionLocation());
				if (event.character == SWT.TAB) {
					currentState.onKeyUp();
					proposalsProvider.stopCompletion();
				}
			}
		});
	}


	private void complete() {
		assert list.getItems().length > 0;
		assert 0 <= list.getCurrentSelectionIndex() && list.getCurrentSelectionIndex() < list.getItems().length;

		list.hide();
		CompletionProposal proposal = proposals.get(list.getCurrentSelectionIndex());
		proposal.applyTo(document.getCurrentPosition());
	}

	private Point completionLocation() {
		Control control= text.getCaret().getParent();
		Point pt = new Point(text.getCaret().getLocation().x,text.getCaret().getLocation().y);
		 while (control != null) {
			pt.x += control.getLocation().x;
			pt.y += control.getLocation().y;
			control = control.getParent();
		}
		return pt;
	}

	public void setProposals(List<? extends CompletionProposal> proposals) {
		this.proposals = proposals;
		String[] strings = new String[proposals.size()];
		int i = 0;
		for (CompletionProposal proposal : proposals) {
			strings[i++] = proposal.completetion();
		}
		list.setItems(strings);
//		if (proposals.size() == 0 || proposals.size() == 1) {
//			complete();
//		}
	}
}
