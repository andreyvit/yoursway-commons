package com.yoursway.utils.strings;

import java.util.Iterator;

public class ChainedIterator<T> implements Iterator<T> {
    
    private final Iterator<T> first;
    private final Iterator<T> second;
    private boolean servedFromSecond = false;
    
    public ChainedIterator(Iterator<T> first, Iterator<T> second) {
        this.first = first;
        this.second = second;
    }
    
    public boolean hasNext() {
        return first.hasNext() || second.hasNext();
    }
    
    public T next() {
        if (first.hasNext()) {
            servedFromSecond = false;
            return first.next();
        } else {
            servedFromSecond = true;
            return second.next();
        }
    }
    
    public void remove() {
        if (servedFromSecond)
            second.remove();
        else
            first.remove();
    }
    
}
