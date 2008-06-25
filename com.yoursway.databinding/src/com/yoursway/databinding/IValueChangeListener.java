package com.yoursway.databinding;


/**
 * Listener for changes to observable values.
 * 
 * @since 1.0
 * 
 */
public interface IValueChangeListener extends IObservablesListener {

	/**
	 * Handles a change to an observable value. The given event object must only
	 * be used locally in this method because it may be reused for other change
	 * notifications. The diff object referenced by the event is immutable and
	 * may be used non-locally.
	 * 
	 * @param event
	 *            the event
	 */
	void handleValueChange(ValueChangeEvent event);

}
