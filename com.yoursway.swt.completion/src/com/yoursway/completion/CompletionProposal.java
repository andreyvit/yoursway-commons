package com.yoursway.completion;

import com.yoursway.document.DocumentPosition;

public interface CompletionProposal {
    /**
     * @return a completion proposal proposal text that should replace text
     *         which it is triggered by.
     * */
    String completetion();
    
    /**
     * Complete at the specified position.
     * @param position
     */
    void applyTo(DocumentPosition position);
    
    void acceptVisitor(CompletionProposalVisitor visitor);
}
