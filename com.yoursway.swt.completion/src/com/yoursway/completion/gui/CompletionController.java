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
import com.yoursway.document.Document;

public class CompletionController implements CompletionProposalUpdatesListener {
	private CompletionProposalsView list;
	private final StyledText text;
	private List<? extends CompletionProposal> proposals;
	private final Document document;
	private boolean tabIsPressed = false;
	
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

		final Listener oldKeyDownListener = text.getListeners(SWT.KeyDown)[0];
		text.removeListener(SWT.KeyDown, oldKeyDownListener); 
		text.addListener(SWT.KeyDown, new Listener() {

			public void handleEvent(Event event) {
				Point listLocation = completionLocation();
				if (event.character == SWT.TAB) {
					tabIsPressed = true;
					if (list.isVisible())
						return;
					proposalsProvider.startCompletionFor(CompletionController.this, text.getText(), text.getCaretOffset());
					if (list.getItems().length > 0)
						list.show(new Rectangle(listLocation.x, listLocation.y, 200, 100),text);
					event.doit = false;
				} else {
					String oldStr = text.getText();
					oldKeyDownListener.handleEvent(event);
					if (text.getText().length() != oldStr.length()) {
						proposalsProvider.startCompletionFor(CompletionController.this, text.getText(), text.getCaretOffset());
						list.setLocation(listLocation);
					}
					if (tabIsPressed && list.getItems().length > 1) {
						list.show(new Rectangle(listLocation.x, listLocation.y, 200, 100),text);
					}
				}
			}
		});
		text.addListener(SWT.KeyUp, new Listener() {

			public void handleEvent(Event event) {
				list.setLocation(completionLocation());
				if (event.character == SWT.TAB) {
					tabIsPressed = false;
					proposalsProvider.stopCompletion();
					complete();
				}
			}
		});
	}

	private void complete() {
		if (!list.isVisible()) {
			return;
		}
		list.hide();
		if (list.getItems().length == 0)
			return;
		assert 0 <= list.getCurrentSelectionIndex() && list.getCurrentSelectionIndex() < list.getItems().length;

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
		if (list.isVisible() && (proposals.size() == 0 || proposals.size() == 1)) {
			complete();
		}
	}
}
