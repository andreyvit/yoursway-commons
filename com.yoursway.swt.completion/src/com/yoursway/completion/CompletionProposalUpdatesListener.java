package com.yoursway.completion;

import java.util.List;

public interface CompletionProposalUpdatesListener {
	void setProposals(List<? extends CompletionProposal> proposals);
}
