package com.yoursway.commons.dependencies.internal;

import java.util.Collection;
import java.util.Iterator;

import com.yoursway.commons.dependencies.IdentityObject;
import com.yoursway.commons.dependencies.Mutable;
import com.yoursway.commons.dependencies.MutableValueObject;

public class ObservableCollection<T> extends MutableValueObject implements Mutable,
		Collection<T> {

	private final Collection<T> collection;

	public ObservableCollection(IdentityObject owner, Collection<T> collection) {
		super(owner);
		if (collection == null)
			throw new NullPointerException("collection is null");
		this.collection = collection;
	}

	public boolean add(T o) {
		boolean result = collection.add(o);
		didChange();
		return result;
	}

	public boolean addAll(Collection<? extends T> c) {
		boolean result = collection.addAll(c);
		didChange();
		return result;
	}

	public void clear() {
		collection.clear();
		didChange();
	}

	public boolean contains(Object o) {
		willQuery();
		return collection.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		willQuery();
		return collection.containsAll(c);
	}

	public boolean equals(Object o) {
		willQuery();
		return collection.equals(o);
	}

	public int hashCode() {
		willQuery();
		return collection.hashCode();
	}

	public boolean isEmpty() {
		willQuery();
		return collection.isEmpty();
	}

	public Iterator<T> iterator() {
		willQuery();
		return collection.iterator();
	}

	public boolean remove(Object o) {
		boolean result = collection.remove(o);
		didChange();
		return result;
	}

	public boolean removeAll(Collection<?> c) {
		boolean result = collection.removeAll(c);
		didChange();
		return result;
	}

	public boolean retainAll(Collection<?> c) {
		boolean result = collection.retainAll(c);
		didChange();
		return result;
	}

	public int size() {
		willQuery();
		return collection.size();
	}

	public Object[] toArray() {
		willQuery();
		return collection.toArray();
	}

	public <X> X[] toArray(X[] a) {
		willQuery();
		return collection.toArray(a);
	}

}
