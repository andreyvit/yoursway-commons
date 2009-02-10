package com.yoursway.commons.dependencies;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.yoursway.commons.dependencies.internal.ObservableMap;
import com.yoursway.utils.disposable.DisposableImpl;
import com.yoursway.utils.disposable.Disposer;

public class AutoMapper<K, V> extends DisposableImpl implements
		Runnable {

	private final Collection<K> keys;

	private final Map<K, V> map = ObservableMap.create(this, new HashMap<K, V>());

	private final Map<K, V> unmodMap = Collections.unmodifiableMap(map);

	private final Mapping<K, V> mapping;

	public AutoMapper(Disposer parent, Collection<K> keys, Mapping<K, V> mapping) {
		super(parent);
		if (keys == null)
			throw new NullPointerException("keys is null");
		if (mapping == null)
			throw new NullPointerException("mapping is null");
		this.keys = keys;
		this.mapping = mapping;
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
				new Rerun(this) {

					public void run() {
						map.put(key, mapping.map(key));
					}

				};
		}
		for (K key : oldKeys)
			mapping.dispose(key, map.remove(key));
	}

}
