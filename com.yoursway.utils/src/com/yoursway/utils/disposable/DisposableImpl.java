package com.yoursway.utils.disposable;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.synchronizedCollection;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.yoursway.utils.bugs.Bugs;

public class DisposableImpl implements Disposable, Disposer {
    
    private volatile boolean disposed = false;
    
    private final Collection<Disposable> disposables = synchronizedCollection(Lists
            .<Disposable> newArrayList());
    
    public final void dispose() {
        disposed = true;
        disposeResources();
        List<Disposable> list = newArrayList(disposables);
        for (Disposable disposable : list)
            try {
                disposable.dispose();
            } catch (Throwable e) {
                Bugs.listenerFailed(e, disposable, "dispose");
            }
        disposables.clear();
    }
    
    public boolean isDisposed() {
        return disposed;
    }
    
    protected void disposeResources() {
    }
    
    public void addDisposeListener(Disposable disposable) {
        if (disposable == null)
            throw new NullPointerException("disposable is null");
        disposables.add(disposable);
    }
    
    public void removeDisposeListener(Disposable disposable) {
        if (disposable == null)
            throw new NullPointerException("disposable is null");
        disposables.remove(disposable);
    }
    
}
