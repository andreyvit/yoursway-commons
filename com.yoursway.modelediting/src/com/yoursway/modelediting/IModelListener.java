package com.yoursway.modelediting;

public interface IModelListener {

	void modelChanged(Object sender, Model model, int firstFragment, int oldCount, int newCount);
	
}
