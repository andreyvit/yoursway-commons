package com.yoursway.commons.dependencies;

public interface Mapping<K, V> {
	
	V map(K key);
	
	void dispose(K key, V value);

}
