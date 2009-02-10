package com.yoursway.utils.disposable;

public interface UndoableDisposer extends Disposer {
    
    void abstainFromDisposing(Disposable disposable);

}
