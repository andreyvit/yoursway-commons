package com.yoursway.commons.dependencies;

import com.yoursway.utils.annotations.SynchronizeExternallyOrUseFromSingleThread;

@SynchronizeExternallyOrUseFromSingleThread
public abstract class ComputedValue<T> extends MutableValueObject implements Value<T> {

	private T value;

	public ComputedValue(IdentityObject owner) {
		super(owner);
		new DependentSection(this) {

			public void run() {
				recompute();
			}
			
		};
	}

	public final T get() {
		willQuery();
		return value;
	}
	
	protected abstract T compute();

	public final void set(T value) {
		throw new UnsupportedOperationException();
	}

	final void recompute() {
		value = compute(); 
		didChange();
	}

}
