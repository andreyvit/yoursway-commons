package com.yoursway.completion.demo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.yoursway.completion.CompletionProposal;
import com.yoursway.completion.CompletionProposalUpdatesListener;
import com.yoursway.completion.CompletionProposalsProvider;

public class DictionaryCompletion implements CompletionProposalsProvider {

	private static final int DEFAULT_PRIORITY = Integer.MAX_VALUE / 2;
	private static final int SUBSTR_DIFF = -1;
	private ArrayList<String> words;

	public DictionaryCompletion() throws IOException {
		File file = new File("resources/dictionary.txt");
		FileReader reader = new FileReader(file);
		LineNumberReader lnr = new LineNumberReader(reader);
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

	public void startCompletionFor(CompletionProposalUpdatesListener listener, String source, int cursorIndex) {

		int beginIndex = (source.lastIndexOf(' ') != -1) ? source.lastIndexOf(' ') + 1: 0;
		String substring = source.substring(beginIndex, cursorIndex);

		List<CompletionProposalImpl> proposals = proposalsList(substring);

		Collections.sort(proposals, new Comparator<CompletionProposalImpl>() {
			public int compare(CompletionProposalImpl o1, CompletionProposalImpl o2) {
				return o1.relevance() - o2.relevance();
			}
		});
		listener.setProposals(proposals);
	}

	public void stopCompletion() {
	}

}
