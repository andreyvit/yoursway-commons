package com.yoursway.completion;

/**
 * Implementations of this interface are intended to (asynchronously) calculate
 * completion proposals and notify listeners about the results.
 * 
 * @author leon
 * 
 */
public interface CompletionProposalsProvider {
	/**
	 * When called repeatedly, this method stops calculation for the listener
	 * registered by previous call.
	 * @param text TODO to be replaced by a kind of source module descriptor.
	 * @param cursorIndex cursor position in the text.
	 * */
	void startCompletionFor(CompletionProposalUpdatesListener listener,
 			String text, int cursorIndex);

	/**
	 * Stop calculation of completion proposals and notifying the listener about
	 * updates.
	 */
	void stopCompletion();

	int getCompletionLength(String text, int caretOffset);
}
