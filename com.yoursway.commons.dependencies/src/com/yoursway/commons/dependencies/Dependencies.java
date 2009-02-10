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

	static void reading(Observable observable) {
		DependencyCollector collector = currentChangeListener.get();
		if (collector != null)
			collector.dependency(observable);
	}

	public static <K, V> Map<K, V> automap(Disposer disposer,
			Collection<K> keys, Mapping<K, V> mapping) {
		return new AutoMapper<K, V>(disposer, keys, mapping).map();
	}

	public static <T extends Disposer> Collection<T> compositionCollection(
			Disposer disposer, Collection<T> storage) {
		return new AutoCollection<T>(disposer, storage);
	}

	public static <T extends Disposer> Collection<T> compositionCollection(
			Disposer disposer) {
		return compositionCollection(disposer, new ArrayList<T>());
	}
	
	public static <T> List<T> observableList(Disposer disposer) {
		return observableList(disposer, new ArrayList<T>());
	}

	public static <T> List<T> observableList(Disposer disposer, List<T> list) {
		return new ObservableList<T>(disposer, list);
	}

	public static <K, V> Map<K, V> observable(Disposer disposer, Map<K, V> map) {
		return new ObservableMap<K, V>(disposer, map);
	}

	public static <T> Value<T> observableValue(Disposer disposer, T initial) {
		return new ObservableValue<T>(disposer, initial);
	}
	
	public static <T> NullableValue<T> observableNullableValue(Disposer disposer) {
		return observableNullableValue(disposer, null);
	}
	
	public static <T> NullableValue<T> observableNullableValue(Disposer disposer, T initial) {
		return new NullableObservableValue<T>(disposer, initial);
	}

}
