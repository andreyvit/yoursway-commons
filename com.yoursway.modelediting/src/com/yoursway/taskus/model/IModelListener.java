package com.yoursway.taskus.model;

public interface IModelListener {

	void modelChanged(int startOffset, int oldLength, int newLength);
	
}
