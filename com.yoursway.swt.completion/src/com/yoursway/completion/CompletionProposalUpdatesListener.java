package com.yoursway.completion;

import java.util.Collection;

public interface CompletionProposalUpdatesListener {
	void setProposals(Collection<CompletionProposal> proposals);
}
