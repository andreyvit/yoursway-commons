package com.yoursway.commons.dependencies.internal;

import com.yoursway.commons.dependencies.ObservableImpl;
import com.yoursway.commons.dependencies.Value;
import com.yoursway.utils.disposable.Disposer;

public class ObservableValue<T> extends ObservableImpl implements Value<T> {

	private T value;

	public ObservableValue(Disposer disposer, T initialValue) {
		super(disposer);
		if (initialValue == null)
			throw new NullPointerException("initialValue is null");
		this.value = initialValue;
	}

	public T get() {
		willQuery();
		return value;
	}

	public void set(T value) {
		if (value == null)
			throw new NullPointerException("value is null");
		this.value = value;
		didChange();
	}

}
