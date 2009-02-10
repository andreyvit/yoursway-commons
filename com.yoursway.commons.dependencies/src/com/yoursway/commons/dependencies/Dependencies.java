package com.yoursway.commons.dependencies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.yoursway.commons.dependencies.internal.AutoCollection;
import com.yoursway.commons.dependencies.internal.DependencyCollector;
import com.yoursway.commons.dependencies.internal.NullableObservableValue;
import com.yoursway.commons.dependencies.internal.ObservableList;
import com.yoursway.commons.dependencies.internal.ObservableMap;
import com.yoursway.commons.dependencies.internal.ObservableValue;
import com.yoursway.utils.AutoThreadLocal;
import com.yoursway.utils.disposable.Disposer;

public class Dependencies {

	private static AutoThreadLocal<DependencyCollector> currentChangeListener = AutoThreadLocal
			.create();

	static void run(Runnable runnable, DependencyCollector collector) {
		currentChangeListener.runWith(runnable, collector);
	}

	static void reading(Mutable observable) {
		DependencyCollector collector = currentChangeListener.get();
		if (collector != null)
			collector.dependency(observable);
	}

	public static <K, V> Map<K, V> automap(IdentityObject owner,
			Collection<K> keys, Mapping<K, V> mapping) {
		return new AutoMapper<K, V>(owner, keys, mapping).map();
	}

	public static <T extends Disposer> Collection<T> compositionCollection(
			IdentityObject owner, Collection<T> storage) {
		return new AutoCollection<T>(owner, storage);
	}

	public static <T extends Disposer> Collection<T> compositionCollection(
			IdentityObject owner) {
		return compositionCollection(owner, new ArrayList<T>());
	}

	public static <T> List<T> observableList(IdentityObject owner) {
		return observableList(owner, new ArrayList<T>());
	}

	public static <T> List<T> observableList(IdentityObject owner, List<T> list) {
		return new ObservableList<T>(owner, list);
	}

	public static <K, V> Map<K, V> observable(IdentityObject owner,
			Map<K, V> map) {
		return new ObservableMap<K, V>(owner, map);
	}

	public static <T> Value<T> observableValue(IdentityObject owner, T initial) {
		return new ObservableValue<T>(owner, initial);
	}

	public static <T> NullableValue<T> observableNullableValue(
			IdentityObject owner) {
		return observableNullableValue(owner, null);
	}

	public static <T> NullableValue<T> observableNullableValue(
			IdentityObject owner, T initial) {
		return new NullableObservableValue<T>(owner, initial);
	}

}
