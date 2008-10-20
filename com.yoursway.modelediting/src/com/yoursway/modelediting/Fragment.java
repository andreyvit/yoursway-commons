package com.yoursway.modelediting;

public interface Fragment {

	@Override
	String toString();

	boolean canReplace(int startOffset, int length);

	void replace(int startOffset, int length, String text) throws ReplaceImpossibleException;
	
	boolean includesStart();
	boolean includesEnd();

}
