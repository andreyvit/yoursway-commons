package com.yoursway.commons.dependencies;

import java.util.ArrayList;
import java.util.Collection;

import com.yoursway.utils.bugs.Bugs;
import com.yoursway.utils.disposable.Disposable;

public class MutableValueObject extends ValueObject implements Mutable {

	private final Collection<Observer> listeners = new ArrayList<Observer>();

	public MutableValueObject(IdentityObject owner) {
		super(owner);
		alsoDispose(new Disposable() {
			public void dispose() {
				didChange();
			}
		});
	}

	protected final void willQuery() {
		Dependencies.reading(this);
	}

	protected final void didChange() {
		for (Observer listener : new ArrayList<Observer>(listeners))
			try {
				listener.observedObjectDidChange();
			} catch (Throwable throwable) {
				Bugs.listenerFailed(throwable, listener, "observableDidChange");
			}
	}

	public final synchronized void subscribe(Observer listener) {
		if (listener == null)
			throw new NullPointerException("listener is null");
		listeners.add(listener);
	}

	public final synchronized void unsubscribe(Observer listener) {
		if (listener == null)
			throw new NullPointerException("listener is null");
		listeners.remove(listener);
	}

}
