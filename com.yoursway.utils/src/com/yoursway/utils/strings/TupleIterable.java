package com.yoursway.utils.strings;

import java.lang.reflect.Array;
import java.util.Iterator;

public class TupleIterable<T> implements Iterable<T[]> {
    
    private final static class TupleIterator<T> implements Iterator<T[]> {
        
        private final Iterable<T>[] elements;
        private final Iterator<T>[] iterators;
        private final T[] values;
        private boolean hasNext = true;
        private final Class<T> klass;
        
        @SuppressWarnings("unchecked")
        public TupleIterator(Class<T> klass, Iterable<T>[] elements) {
            if (klass == null)
                throw new NullPointerException("klass is null");
            if (elements == null)
                throw new NullPointerException("elements is null");
            this.klass = klass;
            this.elements = elements;
            
            iterators = (Iterator<T>[]) new Iterator<?>[elements.length];
            values = (T[]) Array.newInstance(klass, elements.length);
            resetIteratorsStartingWith(0);
        }
        
        public boolean hasNext() {
            return hasNext;
        }
        
        @SuppressWarnings("unchecked")
        public T[] next() {
            T[] result = (T[]) Array.newInstance(klass, values.length);
            System.arraycopy(values, 0, result, 0, values.length);
            advance();
            return result;
        }
        
        private void advance() {
            int incr = findRightmostIteratorThatHasNextValue();
            if (incr < 0)
                hasNext = false;
            else {
                values[incr] = iterators[incr].next();
                resetIteratorsStartingWith(incr + 1);
            }
        }
        
        private int findRightmostIteratorThatHasNextValue() {
            int nextIterator = -1;
            for (int i = iterators.length - 1; i >= 0; i--)
                if (iterators[i].hasNext()) {
                    nextIterator = i;
                    break;
                }
            return nextIterator;
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        private void resetIteratorsStartingWith(int index) {
            for (int i = index; i < iterators.length; i++) {
                iterators[i] = elements[i].iterator();
                if (!iterators[i].hasNext())
                    hasNext = false;
                else
                    values[i] = iterators[i].next();
            }
        }
        
    }
    
    private final Iterable<T>[] elements;
    private final Class<T> klass;
    
    public TupleIterable(Class<T> klass, Iterable<T>[] elements) {
        if (klass == null)
            throw new NullPointerException("klass is null");
        if (elements == null)
            throw new NullPointerException("elements is null");
        this.klass = klass;
        this.elements = elements;
    }
    
    public Iterator<T[]> iterator() {
        return new TupleIterator<T>(klass, elements);
    }
    
}
