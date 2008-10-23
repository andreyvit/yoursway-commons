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

	/**
	 * Commands are ignored when tab is pressed
	 */
	boolean isCommand(char character, int keyCode){
		return character == SWT.CR || (keyCode & SWT.KEYCODE_BIT) != 0;
	}
	
	/**
	 * Completables are applied when tab is pressed
	 */
	boolean isCompletable(char character, int keyCode){
		return proposalsProvider.isCompletable(character) || character == SWT.BS;
	}
	
	/**
	 * Stoppers finish the completion when tab is pressed
	 */
	boolean isStopper(char character, int keyCode){
		return !(isCommand(character, keyCode) || isCompletable(character, keyCode));
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
				} else if (isCommand(event.character, event.keyCode) && !tabNotPressed) {
					event.doit = false;
				} else if (isStopper(event.character, event.keyCode)){
					strategy.tabReleased();
				} else if (isCompletable(event.character, event.keyCode)){
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
		
		int start = proposalsProvider.findStartOfWord(styledText.getText(), end);
		String completion = proposal.completion();
		styledText.replaceTextRange(start, end - start + 1, completion);
		styledText.setCaretOffset(start + completion.length());
		proposalsView.unhookArrowKeys();
	}

	public void show(DisplayState state) {
		proposalsView.setLocation(completionLocation());
		//System.out.println("STATE:", state);
		proposalsView.show(state);
	}
}
