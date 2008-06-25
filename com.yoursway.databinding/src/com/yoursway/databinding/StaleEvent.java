package com.yoursway.databinding;

/**
 * Generic event denoting that the state of an {@link IObservable} object is
 * about to change. Note that this event is only fired when an observable
 * becomes stale, not when it becomes unstale; an observable that becomes
 * unstale should always fire a change event. Staleness can be used (for
 * example) to notify listeners when an observable has started a background
 * thread for updating its state. Clients can safely ignore staleness.
 * 
 * @see IObservable#isStale()
 * 
 * @since 1.0
 * 
 */
public class StaleEvent extends ObservableEvent {

	/**
	 * Creates a new stale event.
	 * 
	 * @param source
	 *            the source observable
	 */
	public StaleEvent(IObservable source) {
		super(source);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3491012225431471077L;

	static final Object TYPE = new Object();

	protected void dispatch(IObservablesListener listener) {
		((IStaleListener) listener).handleStale(this);
	}

	protected Object getListenerType() {
		return TYPE;
	}

}
