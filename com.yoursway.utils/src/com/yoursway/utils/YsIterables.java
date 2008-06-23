package com.yoursway.utils;


import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;


public class YsIterables {
    
    /**
     * @deprecated Use {@link YsCollections#forEnumeration(Enumeration<T>)} instead
     */
    public static <T> Iterable<T> forEnumeration(Enumeration<T> enumeration) {
        return YsCollections.forEnumeration(enumeration);
    }
    
    /**
     * @deprecated Use {@link YsCollections#forIterator(Iterator<T>)} instead
     */
    public static <T> Iterable<T> forIterator(final Iterator<T> iterator) {
        return YsCollections.forIterator(iterator);
    }
    
    /**
     * @deprecated Use {@link YsCollections#sort(Iterable<T>)} instead
     */
    public static <T extends Comparable<? super T>> List<T> sort(Iterable<T> iterable) {
        return YsCollections.sort(iterable);
    }
    
    /**
     * @deprecated Use {@link YsCollections#sort(Iterator<T>)} instead
     */
    public static <T extends Comparable<? super T>> List<T> sort(Iterator<T> iterator) {
        return YsCollections.sort(iterator);
    }

}
