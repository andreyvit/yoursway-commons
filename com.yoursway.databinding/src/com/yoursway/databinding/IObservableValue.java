package com.yoursway.databinding;


/**
 * A value whose changes can be tracked by value change listeners.
 * 
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 *              Clients should instead subclass one of the classes that
 *              implement this interface. Note that direct implementers of this
 *              interface outside of the framework will be broken in future
 *              releases when methods are added to this interface.
 * 
 * @see AbstractObservableValue
 * 
 * @since 1.0
 */
public interface IObservableValue<T> extends IObservable {

	/**
	 * The value type of this observable value, or <code>null</code> if this
	 * observable value is untyped.
	 * 
	 * @return the value type, or <code>null</null>
	 */
	public Object getValueType();

	/**
	 * Returns the value.  Must be invoked in the {@link Realm} of the observable.
	 * 
	 * @return the current value
	 * @TrackedGetter
	 */
	public T getValue();

	/**
	 * Sets the value.  Must be invoked in the {@link Realm} of the observable.
	 * 
	 * @param value
	 *            the value to set
	 * @throws UnsupportedOperationException
	 *             if this observable value cannot be set.
	 */
	public void setValue(T value);

	/**
	 * 
	 * @param listener
	 */
	public void addValueChangeListener(IValueChangeListener listener);

	/**
	 * @param listener
	 */
	public void removeValueChangeListener(IValueChangeListener listener);
}
