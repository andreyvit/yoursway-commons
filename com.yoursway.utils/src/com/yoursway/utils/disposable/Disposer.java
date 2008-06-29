package com.yoursway.utils.disposable;

public interface Disposer {
    
    void addDisposeListener(Disposable disposable);
    
    void removeDisposeListener(Disposable disposable);
    
}
