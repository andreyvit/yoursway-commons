package com.yoursway.databinding;

/**
 * Mutable (writable) implementation of {@link IObservableValue} that will maintain a value and fire
 * change events when the value changes.
 * <p>
 * This class is thread safe. All state accessing methods must be invoked from
 * the {@link Realm#isCurrent() current realm}. Methods for adding and removing
 * listeners may be invoked from any thread.
 * </p>
 * @since 1.0
 */
public class WritableValue<T> extends AbstractObservableValue<T> {

	private final Object valueType;

	/**
	 * Constructs a new instance with the default realm, a <code>null</code>
	 * value type, and a <code>null</code> value.
	 */
	public WritableValue() {
		this(null, null);
	}

	/**
	 * Constructs a new instance with the default realm.
	 * 
	 * @param initialValue
	 *            can be <code>null</code>
	 * @param valueType
	 *            can be <code>null</code>
	 */
	public WritableValue(T initialValue, Object valueType) {
		this(Realm.getDefault(), initialValue, valueType);
	}

	/**
	 * Constructs a new instance with the provided <code>realm</code>, a
	 * <code>null</code> value type, and a <code>null</code> initial value.
	 * 
	 * @param realm
	 */
	public WritableValue(Realm realm) {
		this(realm, null, null);
	}

	/**
	 * Constructs a new instance.
	 * 
	 * @param realm
	 * @param initialValue
	 *            can be <code>null</code>
	 * @param valueType
	 *            can be <code>null</code>
	 */
	public WritableValue(Realm realm, T initialValue, Object valueType) {
		super(realm);
		this.valueType = valueType;
		this.value = initialValue;
	}

	private T value = null;

	public T doGetValue() {
		return value;
	}

	/**
	 * @param value
	 *            The value to set.
	 */
	public void doSetValue(T value) {
        boolean changed = false;

        if (this.value == null && value != null) {
            changed = true;
        } else if (this.value != null && !this.value.equals(value)) {
            changed = true;
        }

        if (changed) {
            fireValueChange(Diffs.createValueDiff(this.value, this.value = value));
        }
	}

	public Object getValueType() {
		return valueType;
	}

	/**
	 * @param elementType can be <code>null</code>
	 * @return new instance with the default realm and a value of <code>null</code>
	 */
	public static <T> WritableValue<T> withValueType(Class<T> elementType) {
		return new WritableValue<T>(Realm.getDefault(), null, elementType);
	}
	
	/**
	 * @param elementType can be <code>null</code>
	 * @return new instance with the default realm and a value of <code>null</code>
	 */
	public static <T> WritableValue<T> withValue(T initialValue, Class<T> elementType) {
	    return new WritableValue<T>(Realm.getDefault(), initialValue, elementType);
	}
	
}
