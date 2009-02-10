package com.yoursway.commons.dependencies.internal;

import com.yoursway.commons.dependencies.IdentityObject;
import com.yoursway.commons.dependencies.MutableValueObject;
import com.yoursway.commons.dependencies.NullableValue;

public class NullableObservableValue<T> extends MutableValueObject implements NullableValue<T> {

	private T value;

	public NullableObservableValue(IdentityObject owner, T initialValue) {
		super(owner);
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
