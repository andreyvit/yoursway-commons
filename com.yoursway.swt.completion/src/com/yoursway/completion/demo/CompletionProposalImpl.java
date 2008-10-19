package com.yoursway.completion.demo;

import com.yoursway.completion.CompletionProposal;
import com.yoursway.completion.CompletionProposalVisitor;

public class CompletionProposalImpl implements CompletionProposal {

	private final String text;
	private final int relevance;

	public CompletionProposalImpl(String text, int relevance) {
		this.text = text;
		this.relevance = relevance;
	}

	public void acceptVisitor(CompletionProposalVisitor visitor) {
		SimpleCompletionProposalVisitor simpleVisitor = (SimpleCompletionProposalVisitor) visitor;
		simpleVisitor.visit(this);
	}

	public String completion() {
		return text;
	}

	public int relevance() {
		return relevance;
	}
	
	@Override
	public String toString() {
		return completion();
	}
}
