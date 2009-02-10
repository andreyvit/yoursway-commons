package com.yoursway.commons.dependencies;

import java.util.ArrayList;
import java.util.Collection;

import com.yoursway.utils.bugs.Bugs;
import com.yoursway.utils.disposable.DisposableImpl;
import com.yoursway.utils.disposable.Disposer;

public class ObservableImpl extends DisposableImpl implements
		Observable {

	private final Collection<Observer> listeners = new ArrayList<Observer>();

	public ObservableImpl(Disposer parent) {
		super(parent);
	}

	public ObservableImpl(Collection<Disposer> parents) {
		super(parents);
	}

	@Override
	protected final void deliverDisposedNotification() {
		didChange();
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
