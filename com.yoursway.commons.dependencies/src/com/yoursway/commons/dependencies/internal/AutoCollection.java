package com.yoursway.commons.dependencies.internal;

import java.util.Collection;
import java.util.Iterator;

import com.yoursway.commons.dependencies.IdentityObject;
import com.yoursway.utils.disposable.Disposable;
import com.yoursway.utils.disposable.Disposer;
import com.yoursway.utils.disposable.UndoableDisposer;

public class AutoCollection<T extends Disposer> implements Collection<T>, Disposer {

	private final ObservableCollection<T> collection;

	public AutoCollection(IdentityObject owner, Collection<T> storage) {
		this.collection = new ObservableCollection<T>(owner, storage);
	}

	public boolean addAll(Collection<? extends T> c) {
		boolean modified = false;
		Iterator<? extends T> e = c.iterator();
		while (e.hasNext()) {
			if (add(e.next()))
				modified = true;
		}
		return modified;
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public boolean contains(Object o) {
		return collection.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return collection.containsAll(c);
	}

	public boolean isEmpty() {
		return collection.isEmpty();
	}

	public Iterator<T> iterator() {
		return collection.iterator();
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		return collection.size();
	}

	public Object[] toArray() {
		return collection.toArray();
	}

	public <K> K[] toArray(K[] a) {
		return collection.toArray(a);
	}

	public boolean add(final T o) {
		if (o == null)
			throw new NullPointerException("item is null");
		if (!collection.add(o))
			return false;
		o.alsoDispose(new Disposable() {
			public void dispose() {
				collection.remove(o);
			}
		});
		return true;
	}

	public UndoableDisposer alsoDispose(Disposable disposable) {
		return collection.alsoDispose(disposable);
	}

}
