package com.yoursway.databinding;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ListenerList;

/**
 * Listener management implementation. Exposed to subclasses in form of
 * {@link AbstractObservable} and {@link ChangeSupport}.
 * 
 * @since 1.0
 * 
 */
/* package */ class ChangeManager {

	ListenerList[] listenerLists = null;
	Object listenerTypes[] = null;
	private Realm realm;

	/**
	 * @param realm 
	 * 
	 */
	/* package */ ChangeManager(Realm realm) {
		Assert.isNotNull(realm, "Realm cannot be null"); //$NON-NLS-1$
		this.realm = realm;
	}

	/**
	 * @param listenerType
	 * @param listener
	 */
	protected void addListener(Object listenerType,
			IObservablesListener listener) {
		int listenerTypeIndex = findListenerTypeIndex(listenerType);
		if (listenerTypeIndex == -1) {
			int length;
			if (listenerTypes == null) {
				length = 0;
				listenerTypes = new Object[1];
				listenerLists = new ListenerList[1];
			} else {
				length = listenerTypes.length;
				System.arraycopy(listenerTypes, 0,
						listenerTypes = new Object[length + 1], 0, length);
				System
						.arraycopy(listenerLists, 0,
								listenerLists = new ListenerList[length + 1],
								0, length);
			}
			listenerTypes[length] = listenerType;
			listenerLists[length] = new ListenerList();
			boolean hadListeners = hasListeners();
			listenerLists[length].add(listener);
			if (!hadListeners) {
				this.firstListenerAdded();
			}
			return;
		}
		ListenerList listenerList = listenerLists[listenerTypeIndex];
		boolean hadListeners = true;
		if (listenerList.size() == 0) {
			hadListeners = hasListeners();
		}
		listenerList.add(listener);
		if (!hadListeners) {
			firstListenerAdded();
		}
	}

	/**
	 * @param listenerType
	 * @param listener
	 */
	protected void removeListener(Object listenerType,
			IObservablesListener listener) {
		int listenerTypeIndex = findListenerTypeIndex(listenerType);
		if (listenerTypeIndex != -1) {
			listenerLists[listenerTypeIndex].remove(listener);
			if (listenerLists[listenerTypeIndex].size() == 0) {
				if (!hasListeners()) {
					this.lastListenerRemoved();
				}
			}
		}
	}

	protected boolean hasListeners() {
		if (listenerTypes == null) {
			return false;
		}
		for (int i = 0; i < listenerTypes.length; i++) {
			if (listenerLists[i].size() > 0) {
				return true;
			}
		}
		return false;
	}

	private int findListenerTypeIndex(Object listenerType) {
		if (listenerTypes != null) {
			for (int i = 0; i < listenerTypes.length; i++) {
				if (listenerTypes[i] == listenerType) {
					return i;
				}
			}
		}
		return -1;
	}

	protected void fireEvent(ObservableEvent event) {
		Object listenerType = event.getListenerType();
		int listenerTypeIndex = findListenerTypeIndex(listenerType);
		if (listenerTypeIndex != -1) {
			Object[] listeners = listenerLists[listenerTypeIndex]
					.getListeners();
			for (int i = 0; i < listeners.length; i++) {
				event.dispatch((IObservablesListener) listeners[i]);
			}
		}
	}

	/**
	 * 
	 */
	protected void firstListenerAdded() {
	}

	/**
	 * 
	 */
	protected void lastListenerRemoved() {
	}

	/**
	 * 
	 */
	public void dispose() {
		listenerLists = null;
		listenerTypes = null;
		realm = null;
	}

	/**
	 * @return Returns the realm.
	 */
	public Realm getRealm() {
		return realm;
	}

}
