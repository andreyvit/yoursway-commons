package com.yoursway.modelediting.swt.demo;

public class StaticTextFragment extends TextFragment {

	public StaticTextFragment(String text) {
		super(text);
	}

	@Override
	public boolean canReplace(int startOffset, int length) {
		return false;
	}

}
