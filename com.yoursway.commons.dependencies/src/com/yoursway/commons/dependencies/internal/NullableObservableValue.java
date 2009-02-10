package com.yoursway.commons.dependencies.internal;

import com.yoursway.commons.dependencies.NullableValue;
import com.yoursway.commons.dependencies.ObservableImpl;
import com.yoursway.utils.disposable.Disposer;

public class NullableObservableValue<T> extends ObservableImpl implements NullableValue<T> {

	private T value;

	public NullableObservableValue(Disposer disposer, T initialValue) {
		super(disposer);
		this.value = initialValue;
	}

	public T get() {
		willQuery();
		return value;
	}
	
	public boolean isNull() {
		willQuery();
		return value == null;
	}

	public void set(T value) {
		this.value = value;
		didChange();
	}

}
