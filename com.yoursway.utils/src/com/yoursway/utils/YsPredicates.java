package com.yoursway.utils;

import java.util.Collection;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class YsPredicates {
    
    public static <K, V> Predicate<Entry<K, V>> predicateOnKey(final Predicate<K> pred) {
        return new Predicate<Entry<K, V>>() {
            
            public boolean apply(Entry<K, V> t) {
                return pred.apply(t.getKey());
            }
            
        };
    }
    
    public static <K, V> Predicate<Entry<K, V>> predicateOnValue(final Predicate<V> pred) {
        return new Predicate<Entry<K, V>>() {
            
            public boolean apply(Entry<K, V> t) {
                return pred.apply(t.getValue());
            }
            
        };
    }
    
    public static <T, C extends Collection<T>> Predicate<C> contains(final T value) {
        return new Predicate<C>() {
            
            public boolean apply(C t) {
                return t.contains(value);
            }
            
        };
    }
    
    public static <K, V> Function<Entry<K, V>, K> keyOfEntry() {
        return new Function<Entry<K,V>, K>() {

            public K apply(Entry<K, V> from) {
                return from.getKey();
            }
            
        };
    }
    
    public static <K, V> Function<Entry<K, V>, V> valueOfEntry() {
        return new Function<Entry<K,V>, V>() {
            
            public V apply(Entry<K, V> from) {
                return from.getValue();
            }
            
        };
    }
 
    
}
