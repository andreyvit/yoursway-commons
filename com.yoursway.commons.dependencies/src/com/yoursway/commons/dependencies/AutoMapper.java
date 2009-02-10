package com.yoursway.commons.dependencies;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AutoMapper<K, V> extends ValueObject implements Runnable {

	private final Collection<K> keys;

	private final Map<K, V> map;

	private final Map<K, V> unmodMap;

	private final Mapping<K, V> mapping;

	public AutoMapper(IdentityObject owner, Collection<K> keys,
			Mapping<K, V> mapping) {
		super(owner);
		if (keys == null)
			throw new NullPointerException("keys is null");
		if (mapping == null)
			throw new NullPointerException("mapping is null");
		this.keys = keys;
		this.mapping = mapping;
		this.map = Dependencies.observable(owner, new HashMap<K, V>());
		this.unmodMap = Collections.unmodifiableMap(map);
	}

	public Map<K, V> map() {
		return unmodMap;
	}

	public void run() {
		Set<K> oldKeys = new HashSet<K>(map.keySet());
		for (final K key : keys) {
			if (map.containsKey(key))
				oldKeys.remove(key);
			else
				new DependentSection(this) {

					public void run() {
						map.put(key, mapping.map(key));
					}

				};
		}
		for (K key : oldKeys)
			mapping.dispose(key, map.remove(key));
	}

}
