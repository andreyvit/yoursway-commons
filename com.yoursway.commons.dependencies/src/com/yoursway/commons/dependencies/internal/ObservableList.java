package com.yoursway.commons.dependencies.internal;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.yoursway.commons.dependencies.IdentityObject;
import com.yoursway.commons.dependencies.Mutable;
import com.yoursway.commons.dependencies.MutableValueObject;

public class ObservableList<T> extends MutableValueObject implements Mutable,
		List<T> {

	private final List<T> list;

	public ObservableList(IdentityObject owner, List<T> list) {
		super(owner);
		if (list == null)
			throw new NullPointerException("list is null");
		this.list = list;
	}

	public void add(int index, T element) {
		list.add(index, element);
		didChange();
	}

	public boolean add(T o) {
		boolean result = list.add(o);
		didChange();
		return result;
	}

	public boolean addAll(Collection<? extends T> c) {
		boolean result = list.addAll(c);
		didChange();
		return result;
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		boolean result = list.addAll(index, c);
		didChange();
		return result;
	}

	public void clear() {
		list.clear();
		didChange();
	}

	public boolean contains(Object o) {
		willQuery();
		return list.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		willQuery();
		return list.containsAll(c);
	}

	public boolean equals(Object o) {
		willQuery();
		return list.equals(o);
	}

	public T get(int index) {
		willQuery();
		return list.get(index);
	}

	public int hashCode() {
		willQuery();
		return list.hashCode();
	}

	public int indexOf(Object o) {
		willQuery();
		return list.indexOf(o);
	}

	public boolean isEmpty() {
		willQuery();
		return list.isEmpty();
	}

	public Iterator<T> iterator() {
		willQuery();
		return list.iterator();
	}

	public int lastIndexOf(Object o) {
		willQuery();
		return list.lastIndexOf(o);
	}

	public ListIterator<T> listIterator() {
		willQuery();
		return list.listIterator();
	}

	public ListIterator<T> listIterator(int index) {
		willQuery();
		return list.listIterator(index);
	}

	public T remove(int index) {
		T result = list.remove(index);
		didChange();
		return result;
	}

	public boolean remove(Object o) {
		boolean result = list.remove(o);
		didChange();
		return result;
	}

	public boolean removeAll(Collection<?> c) {
		boolean result = list.removeAll(c);
		didChange();
		return result;
	}

	public boolean retainAll(Collection<?> c) {
		boolean result = list.retainAll(c);
		didChange();
		return result;
	}

	public T set(int index, T element) {
		T result = list.set(index, element);
		didChange();
		return result;
	}

	public int size() {
		willQuery();
		return list.size();
	}

	public List<T> subList(int fromIndex, int toIndex) {
		willQuery();
		return list.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		willQuery();
		return list.toArray();
	}

	public <X> X[] toArray(X[] a) {
		willQuery();
		return list.toArray(a);
	}

}
