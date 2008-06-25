package com.yoursway.databinding;


/**
 * Value change event describing a change of an {@link IObservableValue}
 * object's current value.
 * 
 * @since 1.0
 * 
 */
public class ValueChangeEvent extends ObservableEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2305345286999701156L;

	static final Object TYPE = new Object();

	/**
	 * Description of the change to the source observable value. Listeners must
	 * not change this field.
	 */
	public ValueDiff diff;

	/**
	 * Creates a new value change event.
	 * 
	 * @param source
	 *            the source observable value
	 * @param diff
	 *            the value change
	 */
	public ValueChangeEvent(IObservableValue source, ValueDiff diff) {
		super(source);
		this.diff = diff;
	}

	/**
	 * Returns the observable value from which this event originated.
	 * 
	 * @return returns the observable value from which this event originated
	 */
	public IObservableValue getObservableValue() {
		return (IObservableValue) source;
	}

	protected void dispatch(IObservablesListener listener) {
		((IValueChangeListener) listener).handleValueChange(this);
	}

	protected Object getListenerType() {
		return TYPE;
	}

}
