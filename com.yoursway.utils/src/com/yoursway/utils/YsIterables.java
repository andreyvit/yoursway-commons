package com.yoursway.utils;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterators;

public class YsIterables {
    
    public static <T> Iterable<T> forEnumeration(Enumeration<T> enumeration) {
        return forIterator(Iterators.forEnumeration(enumeration));
    }
    
    public static <T> Iterable<T> forIterator(final Iterator<T> iterator) {
        return new Iterable<T>() {
            
            private boolean used = false;

            public Iterator<T> iterator() {
                if (used)
                    throw new IllegalStateException("Cannot obtain this iterable's iterator twice");
                used = true;
                return iterator;
            }
            
        };
    }
    
    public static <T extends Comparable<? super T>> List<T> sort(Iterable<T> iterable) {
        return sort(iterable.iterator());
    }
    
    public static <T extends Comparable<? super T>> List<T> sort(Iterator<T> iterator) {
        List<T> list = newArrayList(iterator);
        Collections.sort(list);
        return list;
    }

}
