package com.yoursway.utils.disposable;

public interface Disposer {
    
	UndoableDisposer alsoDispose(Disposable disposable);
    
}
