package com.yoursway.completion;

public interface CompletionProposal {
    /**
     * @return a completion proposal proposal text that should replace text
     *         which it is triggered by.
     * */
    String completetion();
    
    int relevance();

    void acceptVisitor(CompletionProposalVisitor visitor);
}
