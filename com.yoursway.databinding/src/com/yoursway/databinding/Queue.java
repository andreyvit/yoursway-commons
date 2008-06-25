package com.yoursway.databinding;

/**
 * Created to avoid a dependency on java.util.LinkedList, see bug 205224.
 * 
 * @since 1.1
 * 
 */
public class Queue {

	static class Entry {
		Object object;

		Entry(Object o) {
			this.object = o;
		}

		Entry next;
	}

	Entry first;
	Entry last;

	/**
	 * Adds the given object to the end of the queue.
	 * 
	 * @param o
	 */
	public void enqueue(Object o) {
		Entry oldLast = last;
		last = new Entry(o);
		if (oldLast != null) {
			oldLast.next = last;
		} else {
			first = last;
		}
	}

	/**
	 * Returns the first object in the queue. The queue must not be empty.
	 * 
	 * @return the first object
	 */
	public Object dequeue() {
		Entry oldFirst = first;
		if (oldFirst == null) {
			throw new IllegalStateException();
		}
		first = oldFirst.next;
		if (first == null) {
			last = null;
		}
		oldFirst.next = null;
		return oldFirst.object;
	}

	/**
	 * Returns <code>true</code> if the list is empty.
	 * 
	 * @return <code>true</code> if the list is empty
	 */
	public boolean isEmpty() {
		return first == null;
	}
}