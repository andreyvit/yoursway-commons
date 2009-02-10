package com.yoursway.commons.dependencies;

public interface Value<T> {

	T get();

	void set(T value);

}
