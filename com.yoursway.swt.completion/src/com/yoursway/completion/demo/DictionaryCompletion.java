package com.yoursway.completion.demo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import com.yoursway.completion.Activator;
import com.yoursway.completion.CompletionProposalUpdatesListener;
import com.yoursway.completion.CompletionProposalsProvider;

public class DictionaryCompletion implements CompletionProposalsProvider {

	private static final int DEFAULT_PRIORITY = Integer.MAX_VALUE / 2;
	private static final int SUBSTR_DIFF = -1;
	private ArrayList<String> words;

	public DictionaryCompletion() throws IOException {
		URL dictionary = Activator.getInstance().getBundle().getEntry("resources/dictionary.txt");
		InputStreamReader inputStreamReader = new InputStreamReader(dictionary.openStream());
		LineNumberReader lnr = new LineNumberReader(inputStreamReader);
		words = new ArrayList<String>();
		for (String line = lnr.readLine(); line != null; line = lnr.readLine()) {
			words.add(line);
		}
		lnr.close();
		Collections.sort(words);
	}

	private List<CompletionProposalImpl> proposalsList(String substring) {
		List<CompletionProposalImpl> proposals = new LinkedList<CompletionProposalImpl>();
		System.out.println("filtering string is: " + substring);
		for (String str : words) {
		    CompletionProposalImpl newProposal = null;
			if (str.startsWith(substring))
				newProposal = new CompletionProposalImpl(str, DEFAULT_PRIORITY);
			else if (str.indexOf(substring) != -1)
				newProposal = new CompletionProposalImpl(str, DEFAULT_PRIORITY + SUBSTR_DIFF);
			//TODO continue
			if (newProposal != null)
				proposals.add(newProposal);
		}
		System.out.println("Proposals are: " + proposals);
		return proposals;
	}

	public boolean isCompletable(char c){
		return Character.isLetterOrDigit(c) || c == '_';
	}
	
	public void startCompletionFor(final CompletionProposalUpdatesListener listener, String text, int caretOffset) {
		int beginIndex = findStartOfWord(text, caretOffset);
		String substring = text.substring(beginIndex, caretOffset);

		final List<CompletionProposalImpl> proposals = proposalsList(substring);

		Collections.sort(proposals, new Comparator<CompletionProposalImpl>() {
			public int compare(CompletionProposalImpl o1, CompletionProposalImpl o2) {
				return o1.relevance() - o2.relevance();
			}
		});
		Display.getDefault().timerExec(500, new Runnable(){

			public void run() {
				listener.setProposals(proposals);
			}
		});
	}

	private int findStartOfWord(String text, int caretOffset) {
		int beginIndex = caretOffset - 1;
		while(beginIndex>=0 && isCompletable(text.charAt(beginIndex))){
			beginIndex --;
		}
		return beginIndex + 1;
	}

	public void stopCompletion() {
	}

	public int getCompletionLength(String text, int caretOffset) {
		return caretOffset - findStartOfWord(text, caretOffset);
	}

}
