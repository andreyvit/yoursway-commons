package com.yoursway.utils.instrusive;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface IntrusiveMap<K, V> {
    
    int size();

    boolean isEmpty();

    boolean containsKey(K key);

    boolean containsValue(V value);

    V get(K key);

    // Modification Operations

    V add(V value);
    
    V removeKey(K key);
    
    V removeValue(V value);

    // Bulk Operations

    void putAll(Collection<? extends V> t);

    void clear();

    // Views
    
    Map<K, V> asMap();

    Set<K> keySet();

    Collection<V> values();

    Set<Map.Entry<K, V>> entrySet();

    // Comparison and hashing

    boolean equals(Object o);

    int hashCode();
    
}
