package com.yoursway.taskus.model;

import static com.yoursway.utils.Listeners.newListenersByIdentity;

import java.util.ArrayList;
import java.util.List;

import com.yoursway.utils.Listeners;

public class Model {

	private List<Fragment> fragments = new ArrayList<Fragment>();

	List<Fragment> fragments() {
		return fragments;
	}

	private transient Listeners<IModelListener> listeners = newListenersByIdentity();

	public synchronized void addListener(IModelListener listener) {
		listeners.add(listener);
	}

	public synchronized void removeListener(IModelListener listener) {
		listeners.remove(listener);
	}

	public boolean canReplace(int startOffset, int length) {
		if (startOffset < 0 || length < 0 || startOffset + length > fragments.size())
			return false;
		
		return true;
	}

	public void replace(int startOffset, int length, List<Fragment> newFragments)
			throws ReplaceImpossibleException {
		if (newFragments == null)
			throw new NullPointerException("newFragments is null");
		if (startOffset < 0 || length < 0 || startOffset + length > fragments.size())
			throw new IndexOutOfBoundsException();
		if (!canReplace(startOffset, length))
			throw new ReplaceImpossibleException();

		for (int i = 0; i < length; i++) {
			fragments.remove(startOffset);
		}

		fragments.addAll(startOffset, newFragments);

		for (IModelListener listener : listeners)
			listener.modelChanged(startOffset, length, newFragments.size());
	}

}
