package com.yoursway.databinding;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.AssertionFailedException;

/**
 * @since 1.0
 */
public abstract class AbstractObservable extends ChangeManager implements IObservable {
	
	/**
	 * @param realm
	 */
	public AbstractObservable(Realm realm) {
		super(realm);
	}

	public synchronized void addChangeListener(IChangeListener listener) {
		addListener(ChangeEvent.TYPE, listener);
	}

	public synchronized void removeChangeListener(IChangeListener listener) {
		removeListener(ChangeEvent.TYPE, listener);
	}

	public synchronized void addStaleListener(IStaleListener listener) {
		addListener(StaleEvent.TYPE, listener);
	}

	public synchronized void removeStaleListener(IStaleListener listener) {
		removeListener(StaleEvent.TYPE, listener);
	}

	protected void fireChange() {
		checkRealm();
		fireEvent(new ChangeEvent(this));
	}

	protected void fireStale() {
		checkRealm();
		fireEvent(new StaleEvent(this));
	}

	/**
	 * 
	 */
	public synchronized void dispose() {
		super.dispose();
	}

	/**
	 * Asserts that the realm is the current realm.
	 * 
	 * @see Realm#isCurrent()
	 * @throws AssertionFailedException if the realm is not the current realm
	 */
	protected void checkRealm() {
		Assert.isTrue(getRealm().isCurrent(),
				"This operation must be run within the observable's realm"); //$NON-NLS-1$
	}
}
