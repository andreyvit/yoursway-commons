package com.yoursway.completion.demo;

import com.yoursway.completion.CompletionProposalVisitor;

public abstract class SimpleCompleionProposalVisitor implements
	CompletionProposalVisitor {
    public abstract void visit(CompletionProposalImpl proposal);
}
