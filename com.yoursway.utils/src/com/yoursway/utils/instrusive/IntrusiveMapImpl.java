package com.yoursway.utils.instrusive;

import static com.google.common.collect.Maps.uniqueIndex;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;

class IntrusiveMapImpl<K, V> implements IntrusiveMap<K, V>, Serializable {

    private static final long serialVersionUID = 1L;
    
    private Map<K, V> impl;

    private final Function<V, K> valueToKey;

    IntrusiveMapImpl(Map<K, V> backingMap, Function<V, K> valueToKey) {
        if (backingMap == null)
            throw new NullPointerException("backingMap is null");
        if (valueToKey == null)
            throw new NullPointerException("valueToKey is null");
        this.impl = backingMap;
        this.valueToKey = valueToKey;
    }

    public void clear() {
        impl.clear();
    }

    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return impl.entrySet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        IntrusiveMapImpl other = (IntrusiveMapImpl) obj;
        if (impl == null) {
            if (other.impl != null)
                return false;
        } else if (!impl.equals(other.impl))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((impl == null) ? 0 : impl.hashCode());
        return result;
    }

    public boolean isEmpty() {
        return impl.isEmpty();
    }

    public Set<K> keySet() {
        return impl.keySet();
    }

    public V put(K key, V value) {
        return impl.put(key, value);
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        impl.putAll(m);
    }

    public V remove(Object key) {
        return impl.remove(key);
    }

    public int size() {
        return impl.size();
    }

    public String toString() {
        return impl.toString();
    }

    public Collection<V> values() {
        return impl.values();
    }

    public V add(V value) {
        return impl.put(valueToKey.apply(value), value);
    }

    public boolean containsKey(K key) {
        return impl.containsKey(key);
    }

    public boolean containsValue(V value) {
        return containsKey(valueToKey.apply(value)) && impl.containsValue(value);
    }

    public V get(K key) {
        return impl.get(key);
    }

    public void putAll(Collection<? extends V> t) {
        impl.putAll(uniqueIndex(t, valueToKey));
    }

    public V removeKey(K key) {
        return null;
    }

    public V removeValue(V value) {
        return removeKey(valueToKey.apply(value));
    }

    public Map<K, V> asMap() {
        return impl;
    }
    
}
