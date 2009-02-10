package com.yoursway.commons.dependencies.internal;

import com.yoursway.commons.dependencies.IdentityObject;
import com.yoursway.commons.dependencies.MutableValueObject;
import com.yoursway.commons.dependencies.Value;

public class ObservableValue<T> extends MutableValueObject implements Value<T> {

	private T value;

	public ObservableValue(IdentityObject owner, T initialValue) {
		super(owner);
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
