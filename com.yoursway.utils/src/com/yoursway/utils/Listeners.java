package com.yoursway.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class is a thread safe list that is designed for storing lists of
 * listeners. The implementation is optimized for minimal memory footprint,
 * frequent reads and infrequent writes. Modification of the list is
 * synchronized and relatively expensive, while accessing the listeners is very
 * fast. Readers are given access to the underlying array data structure for
 * reading, with the trust that they will not modify the underlying array.
 * <p>
 * <b>dottedmag</b>: underlying implementation changed to ArrayList, so this
 * might be a bit slower now.
 * <p>
 * <a name="same">A listener list handles the <i>same</i> listener being added
 * multiple times, and tolerates removal of listeners that are the same as other
 * listeners in the list. For this purpose, listeners can be compared with each
 * other using either equality or identity, as specified in the list
 * constructor.
 * </p>
 * <p>
 * This class is typed and modified version of
 * org.eclipse.core.runtime.ListenerList
 * </p>
 * <p>
 * The recommended code sequence for notifying all registered listeners of say,
 * <code>FooListener.eventHappened</code>, is:
 * 
 * <pre>
 * for (FooListener listener : myListeners)
 *     listener.eventHappened(event);
 * </pre>
 * 
 * </p>
 */
public final class Listeners<T> implements Iterable<T> {
    
    /**
     * Mode constant (value 0) indicating that listeners should be considered
     * the <a href="#same">same</a> if they are equal.
     */
    public static final int EQUALITY = 0;
    
    /**
     * Mode constant (value 1) indicating that listeners should be considered
     * the <a href="#same">same</a> if they are identical.
     */
    public static final int IDENTITY = 1;
    
    /**
     * Indicates the comparison mode used to determine if two listeners are
     * equivalent
     */
    private final boolean identity = true;
    
    /**
     * The list of listeners. Initially empty but initialized to an array of
     * size capacity the first time a listener is added. Maintains invariant:
     * listeners != null
     */
    private volatile List<T> listeners = Collections.emptyList();
    
    /**
     * Creates a listener list in which listeners are compared using identity.
     */
    public Listeners() {
    }
    
    /**
     * Adds a listener to this list. This method has no effect if the <a
     * href="#same">same</a> listener is already registered.
     * 
     * @param listener
     *            the non-<code>null</code> listener to add
     */
    public synchronized <L extends T> void add(L listener) {
        if (listener == null)
            throw new NullPointerException("listener is null");
        for (T existingListener : listeners)
            if (identity ? listener == existingListener : listener.equals(existingListener))
                return;
        
        List<T> newListeners = new ArrayList<T>(listeners.size() + 1);
        newListeners.addAll(listeners);
        newListeners.add(listener);
        
        //atomic assignment
        listeners = newListeners;
    }
    
    /**
     * Returns an array containing all the registered listeners. The resulting
     * array is unaffected by subsequent adds or removes. If there are no
     * listeners registered, the result is an empty array. Use this method when
     * notifying listeners, so that any modifications to the listener list
     * during the notification will have no effect on the notification itself.
     * <p>
     * Note: Callers of this method <b>must not</b> modify the returned array.
     * 
     * @return the list of registered listeners
     */
    public Iterator<T> iterator() {
        return listeners.iterator();
    }
    
    /**
     * Returns whether this listener list is empty.
     * 
     * @return <code>true</code> if there are no registered listeners, and
     *         <code>false</code> otherwise
     */
    public boolean isEmpty() {
        return listeners.isEmpty();
    }
    
    /**
     * Removes a listener from this list. Has no effect if the <a
     * href="#same">same</a> listener was not already registered.
     * 
     * @param listener
     *            the non-<code>null</code> listener to remove
     */
    public synchronized void remove(T listener) {
        if (listener == null)
            throw new NullPointerException("listener is null");
        
        // Optimization
        if (listeners.isEmpty())
            ;
        else if (listeners.size() == 1) {
            if (isSame(listeners.get(0), listener))
                listeners = Collections.emptyList();
        } else {
            List<T> newListeners = new ArrayList<T>(listeners.size());
            for (T existingListener : listeners)
                if (!isSame(existingListener, listener))
                    newListeners.add(existingListener);
            listeners = newListeners;
        }
    }
    
    private boolean isSame(T a, T b) {
        return identity ? a == b : a.equals(b);
    }
    
    /**
     * Returns the number of registered listeners.
     * 
     * @return the number of registered listeners
     */
    public int size() {
        return listeners.size();
    }
    
    /**
     * Removes all listeners from this list.
     */
    public synchronized void clear() {
        listeners = Collections.emptyList();
    }

    public static <T> Listeners<T> newListenersByIdentity() {
        return new Listeners<T>();
    }
    
}
