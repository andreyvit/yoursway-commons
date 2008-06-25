package com.yoursway.utils.instrusive;

import static com.google.common.collect.Maps.uniqueIndex;

import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class IntrusiveMaps {
    
    public static <K, V> IntrusiveMap<K, V> newIntrusiveHashMap(Function<V, K> valueToKey) {
        return new IntrusiveMapImpl<K, V>(Maps.<K, V> newHashMap(), valueToKey);
    }
    
    public static <K, V> IntrusiveMap<K, V> newIntrusiveHashMap(Collection<? extends V> m,
            Function<V, K> valueToKey) {
        return new IntrusiveMapImpl<K, V>(Maps.<K, V> newHashMap(uniqueIndex(m, valueToKey)), valueToKey);
    }
    
    public static <K, V> IntrusiveMap<K, V> newIntrusiveHashMapWithExpectedSize(int expectedSize,
            Function<V, K> valueToKey) {
        return new IntrusiveMapImpl<K, V>(Maps.<K, V> newHashMapWithExpectedSize(expectedSize), valueToKey);
    }
    
}
