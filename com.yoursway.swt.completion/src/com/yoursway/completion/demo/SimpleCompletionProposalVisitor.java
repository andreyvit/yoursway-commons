package com.yoursway.completion.demo;

import com.yoursway.completion.CompletionProposalVisitor;

public abstract class SimpleCompletionProposalVisitor implements
	CompletionProposalVisitor {
    public abstract void visit(CompletionProposalImpl proposal);
}
