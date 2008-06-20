package com.yoursway.utils;

import java.util.Iterator;

public abstract class AbstractIterator<T> implements Iterator<T> {
    
    private T next;
    
    protected abstract T calculateNext();
    
    public AbstractIterator() {
        next = calculateNext();
    }

    public boolean hasNext() {
        return next != null;
    }

    public T next() {
        T result = next;
        next = calculateNext();
        return result;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
    
}
