package com.yoursway.commons.dependencies.internal;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.yoursway.commons.dependencies.ObservableImpl;
import com.yoursway.utils.disposable.Disposer;

public class ObservableMap<K, V> extends ObservableImpl implements Map<K, V> {

	private final Map<K, V> map;

	public ObservableMap(Disposer disposer, Map<K, V> map) {
		super(disposer);
		this.map = map;
	}

	public static <K, V> ObservableMap<K, V> create(Disposer disposer,
			Map<K, V> map) {
		return new ObservableMap<K, V>(disposer, map);
	}

	public void clear() {
		map.clear();
		didChange();
	}

	public boolean equals(Object o) {
		willQuery();
		return map.equals(o);
	}

	public int hashCode() {
		willQuery();
		return map.hashCode();
	}

	public boolean isEmpty() {
		willQuery();
		return map.isEmpty();
	}

	public int size() {
		willQuery();
		return map.size();
	}

	public boolean containsKey(Object key) {
		willQuery();
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		willQuery();
		return map.containsValue(value);
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		willQuery();
		return map.entrySet();
	}

	public V get(Object key) {
		willQuery();
		return map.get(key);
	}

	public Set<K> keySet() {
		willQuery();
		return map.keySet();
	}

	public V put(K key, V value) {
		V result = map.put(key, value);
		didChange();
		return result;
	}

	public void putAll(Map<? extends K, ? extends V> t) {
		map.putAll(t);
		didChange();
	}

	public V remove(Object key) {
		V result = map.remove(key);
		didChange();
		return result;
	}

	public Collection<V> values() {
		willQuery();
		return map.values();
	}

}
