package com.yoursway.databinding;

/**
 * An object with state that allows to listen for state changes.
 * 
 * <p>
 * Implementations must not manage listeners themselves, listener management
 * must be delegated to a private instance of type {@link ChangeSupport} if it
 * is not inherited from {@link AbstractObservable}.
 * </p>
 * 
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 *              Clients should instead subclass one of the classes in the
 *              framework that implement this interface. Note that direct
 *              implementers of this interface outside of the framework will be
 *              broken in future releases when methods are added to this
 *              interface.
 * 
 * @since 1.0
 * 
 */
public interface IObservable {

	/**
	 * Returns the realm for this observable. Unless otherwise specified,
	 * getters and setters must be accessed from within this realm. Listeners
	 * will be within this realm when they receive events from this observable.
	 * <p>
	 * Because observables can only be accessed from within one realm, and they
	 * always fire events on that realm, their state can be observed in an
	 * incremental way. It is always safe to call getters of an observable from
	 * within a change listener attached to that observable.
	 * </p>
	 * 
	 * @return the realm
	 */
	public Realm getRealm();

	/**
	 * Adds the given change listener to the list of change listeners. Change
	 * listeners are notified about changes of the state of this observable in a
	 * generic way, without specifying the change that happened. To get the
	 * changed state, a change listener needs to query for the current state of
	 * this observable.
	 * 
	 * @param listener
	 */
	public void addChangeListener(IChangeListener listener);

	/**
	 * Removes the given change listener from the list of change listeners. Has
	 * no effect if the given listener is not registered as a change listener.
	 * 
	 * @param listener
	 */
	public void removeChangeListener(IChangeListener listener);

	/**
	 * Adds the given stale listener to the list of stale listeners. Stale
	 * listeners are notified when an observable object becomes stale, not when
	 * is becomes non-stale.
	 * 
	 * @param listener
	 * 
	 * @see #isStale()
	 */
	public void addStaleListener(IStaleListener listener);

	/**
	 * Removes the given stale listener from the list of stale listeners. Has no
	 * effect if the given listener is not registered as a stale listener.
	 * 
	 * @param listener
	 */
	public void removeStaleListener(IStaleListener listener);

	/**
	 * Returns whether the state of this observable is stale and is expected to
	 * change soon. A non-stale observable that becomes stale will notify its
	 * stale listeners. A stale object that becomes non-stale does so by
	 * changing its state and notifying its change listeners, it does <b>not</b>
	 * notify its stale listeners about becoming non-stale. Clients that do not
	 * expect asynchronous changes may ignore staleness of observable objects.
	 * 
	 * @return true if this observable's state is stale and will change soon.
	 * 
	 * @TrackedGetter - implementers must call
	 *                {@link ObservableTracker#getterCalled(IObservable)}.
	 */
	public boolean isStale();

	/**
	 * Disposes of this observable object, removing all listeners registered
	 * with this object, and all listeners this object might have registered on
	 * other objects.
	 */
	public void dispose();
}
