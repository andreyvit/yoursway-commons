package com.yoursway.completion.gui;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.yoursway.completion.CompletionProposal;
import com.yoursway.completion.CompletionProposalUpdatesListener;
import com.yoursway.completion.CompletionProposalsProvider;
import com.yoursway.document.Document;

public class CompletionController implements CompletionProposalUpdatesListener, CompletionProvider {
	private static final int LONG_CLICK_THRESHOLD = 100;
	private ProposalsView proposalsView;
	private final StyledText styledText;

	private final CompletionProposalsProvider proposalsProvider;
	private List<? extends CompletionProposal> proposals;
	public final CompletionStrategy strategy;
	private Timer timer;
	protected boolean tabNotPressed = true;
	private TimerTask task;
	
	/**
	 * 
	 * @param parent
	 *            parent shell for the completion list.
	 * @param styledText
	 *            text editor to enable completion for.
	 */
	 public CompletionController(final StyledText styledText, final CompletionProposalsProvider proposalsProvider) {
		if (styledText == null || proposalsProvider == null)
			throw new IllegalArgumentException();

		this.styledText = styledText;
		this.strategy = new CompletionStrategy(this);
		this.proposalsView = new ProposalsView(styledText, strategy);
		this.proposalsProvider = proposalsProvider;
		
		proposalsView.setSize(new Point(200, 100));
		
		setListeners();
	}

	private TimerTask createLongTabWaiter() {
		return new TimerTask(){
				@Override
				public void run() {
					Display.getDefault().asyncExec(new Runnable(){
						public void run() {	
							strategy.tabLongClickThreshold();
						};
					});
				}
				
			};
	}

	private void setListeners() {
		styledText.addVerifyKeyListener(new VerifyKeyListener(){
			public void verifyKey(VerifyEvent event) {
				if (event.character == SWT.TAB) {
					if(tabNotPressed){
						proposalsView.hookArrowKeys();
						strategy.tabPressed();
						timer.purge();
						task = createLongTabWaiter();
						timer.schedule(task, LONG_CLICK_THRESHOLD);
						tabNotPressed = false;
					}
					event.doit = false;
				} else if (event.character == SWT.CR 
						|| event.keyCode == SWT.ARROW_UP
						|| event.keyCode == SWT.ARROW_DOWN
						|| event.keyCode == SWT.ARROW_LEFT
						|| event.keyCode == SWT.ARROW_RIGHT) 
				{
					if(!tabNotPressed)
						event.doit = false;
				} else {
					strategy.keyPressed();
				}
			}
		});

		styledText.addListener(SWT.KeyUp, new Listener() {

			public void handleEvent(Event event) {
				if (event.character == SWT.TAB) {
					tabNotPressed = true;
					task.cancel();
					timer.purge();
					strategy.tabReleased();
					event.doit = false;
				}
			}
		});

		styledText.addListener(SWT.FocusOut, new Listener() {
			public void handleEvent(Event event) {
				strategy.focusLost();
			}
		});

		timer = new Timer();
	}


	private Point completionLocation() {
		Point location = styledText.getCaret().getLocation();
		Control control= styledText.getCaret().getParent();
		int caretLineHeight = styledText.getLineHeight(styledText.getCaretOffset());
		return control.toDisplay(location.x, location.y + caretLineHeight);
	}

	public void setProposals(List<? extends CompletionProposal> proposals) {
		this.proposals = proposals;
		String[] strings = new String[proposals.size()];
		int i = 0;
		for (CompletionProposal proposal : proposals) {
			strings[i++] = proposal.completion();
		}
		proposalsView.setItems(strings);
		strategy.proposalsCalculated();
	}

	public void cancel() {
		proposalsProvider.stopCompletion();
		proposalsView.unhookArrowKeys();
	}

	public void restart() {
		proposalsProvider.startCompletionFor(CompletionController.this, styledText.getText(), styledText.getCaretOffset());
	}

	public void start() {
		proposalsProvider.startCompletionFor(CompletionController.this, styledText.getText(), styledText.getCaretOffset());
	}

	public void complete() {
		int currentIndex = proposalsView.getSelectionIndex();
		if(currentIndex<0 || currentIndex >= proposalsView.getItems().length){
			return;
		}

		CompletionProposal proposal = proposals.get(currentIndex);
		int end = styledText.getCaretOffset();
		int length = proposalsProvider.getCompletionLength(styledText.getText(), end);
		int start = end - length;
		String completion = proposal.completion();
		styledText.replaceTextRange(start, length, completion);
		styledText.setCaretOffset(start + completion.length());
		proposalsView.unhookArrowKeys();
	}

	public void show(DisplayState state) {
		proposalsView.setLocation(completionLocation());
		//System.out.println("STATE:", state);
		proposalsView.show(state);
	}
}
