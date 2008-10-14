package com.yoursway.modelediting;

public interface IModelListener {

	void modelChanged(Model model, int firstFragment, int oldCount, int newCount);
	
}
