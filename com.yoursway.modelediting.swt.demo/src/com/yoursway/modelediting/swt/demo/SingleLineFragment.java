package com.yoursway.modelediting.swt.demo;

import com.yoursway.modelediting.ReplaceImpossibleException;
import com.yoursway.modelediting.TextFragment;

public class SingleLineFragment extends TextFragment {

	public SingleLineFragment(String text) {
		super(text);
	}

	@Override
	public void replace(int startOffset, int length, String newText) throws ReplaceImpossibleException {
		if (newText.indexOf('\n') != -1)
			throw new ReplaceImpossibleException();
		super.replace(startOffset, length, newText);
	}
	
}
