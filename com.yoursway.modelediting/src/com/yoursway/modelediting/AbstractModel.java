package com.yoursway.modelediting;

import static com.yoursway.utils.Listeners.newListenersByIdentity;

import java.util.ArrayList;
import java.util.List;

import com.yoursway.utils.Listeners;

public abstract class AbstractModel implements Model {

	protected List<Fragment> fragments = new ArrayList<Fragment>();

	public List<Fragment> fragments() {
		return fragments;
	}

	protected transient Listeners<IModelListener> listeners = newListenersByIdentity();

	public synchronized void addListener(IModelListener listener) {
		listeners.add(listener);
	}

	public synchronized void removeListener(IModelListener listener) {
		listeners.remove(listener);
	}

}
