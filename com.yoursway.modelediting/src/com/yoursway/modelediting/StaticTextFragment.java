package com.yoursway.modelediting;

public class StaticTextFragment extends TextFragment {

	public StaticTextFragment(String text) {
		super(text, false, false);
	}

	@Override
	public boolean canReplace(int startOffset, int length) {
		return false;
	}

}
