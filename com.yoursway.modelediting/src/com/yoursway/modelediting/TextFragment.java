/**
 * 
 */
package com.yoursway.modelediting;


public class TextFragment implements Fragment {

	private String text;
	private final boolean includesStart;
	private final boolean includesEnd;

	public TextFragment(String text, boolean includesStart, boolean includesEnd) {
		if (text == null)
			throw new NullPointerException("text is null");
		this.includesStart = includesStart;
		this.includesEnd = includesEnd;
		this.text = text;
	}

	public TextFragment(String text) {
		this(text, true, false);
	}
	
	@Override
	public String toString() {
		return text;
	}

	public void replace(int startOffset, int length, String newText)
			throws ReplaceImpossibleException {
		text = text.substring(0, startOffset) + newText + text.substring(startOffset + length);
	}

	public boolean canReplace(int startOffset, int length) {
		return true;
	}

	public boolean includesEnd() {
		return includesEnd;
	}

	public boolean includesStart() {
		return includesStart;
	}
}