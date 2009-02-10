package com.yoursway.commons.dependencies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.yoursway.commons.dependencies.internal.AutoCollection;
import com.yoursway.utils.disposable.Disposable;
import com.yoursway.utils.disposable.Disposer;
import com.yoursway.utils.disposable.UndoableDisposer;

public class UnorderedOwnedSetImpl<E extends Disposer> implements Disposer {
	
	protected final Collection<E> items;
	
	private final Collection<E> unmodItems;

	public UnorderedOwnedSetImpl(Disposer disposer) {
		items = new AutoCollection<E>(disposer, new ArrayList<E>());
		unmodItems = Collections.unmodifiableCollection(items);
	}
	
	public Collection<E> asCollection() {
		return items;
	}

	public UndoableDisposer alsoDispose(Disposable disposable) {
		return ((Disposer) items).alsoDispose(disposable);
	}
	
}
