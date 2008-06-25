package com.yoursway.databinding;

/**
 * Listener for staleness events. An observable object is stale if its state
 * will change eventually.
 * 
 * @since 1.0
 */
public interface IStaleListener extends IObservablesListener {

	/**
	 * Handle the event that the given observable object is now stale. The given
	 * event object must only be used locally in this method because it may be
	 * reused for other change notifications.
	 * 
	 * @param staleEvent
	 */
	public void handleStale(StaleEvent staleEvent);

}
