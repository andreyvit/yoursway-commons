package com.yoursway.databinding;


/**
 * 
 * <p>
 * This class is thread safe. All state accessing methods must be invoked from
 * the {@link Realm#isCurrent() current realm}. Methods for adding and removing
 * listeners may be invoked from any thread.
 * </p>
 * @since 1.0
 * 
 */
abstract public class AbstractObservableValue<T> extends AbstractObservable implements IObservableValue<T> {
	/**
	 * Constructs a new instance with the default realm.
	 */
	public AbstractObservableValue() {
		this(Realm.getDefault());
	}

	/**
	 * @param realm
	 */
	public AbstractObservableValue(Realm realm) {
		super(realm);
	}

	public synchronized void addValueChangeListener(IValueChangeListener listener) {
		addListener(ValueChangeEvent.TYPE, listener);
	}

	public synchronized void removeValueChangeListener(IValueChangeListener listener) {
		removeListener(ValueChangeEvent.TYPE, listener);
	}

	final public void setValue(T value) {
		checkRealm();
		doSetValue(value);
	}

	/**
	 * Template method for setting the value of the observable. By default the
	 * method throws an {@link UnsupportedOperationException}.
	 * 
	 * @param value
	 */
	protected void doSetValue(T value) {
		throw new UnsupportedOperationException();
	}

	protected void fireValueChange(ValueDiff<T> diff) {
		// fire general change event first
		super.fireChange();
		fireEvent(new ValueChangeEvent(this, diff));
	}

	public final T getValue() {
		getterCalled();
		return doGetValue();
	}

	abstract protected T doGetValue();

	public boolean isStale() {
		getterCalled();
		return false;
	}

	private void getterCalled() {
		ObservableTracker.getterCalled(this);
	}

	protected void fireChange() {
		throw new RuntimeException(
				"fireChange should not be called, use fireValueChange() instead"); //$NON-NLS-1$
	}

	public synchronized void dispose() {
		super.dispose();
	}
}
