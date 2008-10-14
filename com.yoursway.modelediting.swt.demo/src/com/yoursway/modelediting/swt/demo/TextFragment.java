package com.yoursway.modelediting.swt.demo;

import com.yoursway.modelediting.Fragment;
import com.yoursway.modelediting.ReplaceImpossibleException;

public class TextFragment implements Fragment {

	private String text;

	public TextFragment(String text) {
		if (text == null)
			throw new NullPointerException("text is null");
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

	public void replace(int startOffset, int length, String newText) throws ReplaceImpossibleException {
		text = text.substring(0, startOffset) + newText + text.substring(startOffset + length);
	}

	public boolean canReplace(int startOffset, int length) {
		return true;
	}

}
