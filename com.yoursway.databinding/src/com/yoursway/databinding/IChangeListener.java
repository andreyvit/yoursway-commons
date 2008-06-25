
package com.yoursway.databinding;


/**
 * Listener for generic change events. Note that the change events do not carry
 * information about the change, they only specify the affected observable. To
 * listen for specific change events, use more specific change listeners.
 * 
 * @see IValueChangeListener
 * @see IListChangeListener
 * @see ISetChangeListener
 * @see IMapChangeListener
 * 
 * @since 1.0
 */
public interface IChangeListener extends IObservablesListener {

	/**
	 * Handle a generic change to the given observable. The given event object
	 * must only be used locally in this method because it may be reused for
	 * other change notifications.
	 * 
	 * @param event
	 */
	public void handleChange(ChangeEvent event);

}
