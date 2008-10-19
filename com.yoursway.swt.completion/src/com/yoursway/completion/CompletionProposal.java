package com.yoursway.completion;


public interface CompletionProposal {
    /**
     * @return a completion proposal proposal text that should replace text
     *         which it is triggered by.
     * */
    String completion();
    
    void acceptVisitor(CompletionProposalVisitor visitor);
}
