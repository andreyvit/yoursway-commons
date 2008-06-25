package com.yoursway.databinding;


/**
 * @since 1.0
 * 
 */
public abstract class ValueDiff<T> {
	/**
	 * Creates a value diff.
	 */
	public ValueDiff() {
	}

	/**
	 * @return the old value
	 */
	public abstract T getOldValue();

	/**
	 * @return the new value
	 */
	public abstract T getNewValue();

	@SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
		if (obj instanceof ValueDiff) {
			ValueDiff<T> val = (ValueDiff<T>) obj;

			return Diffs.equals(val.getNewValue(), getNewValue())
					&& Diffs.equals(val.getOldValue(), getOldValue());

		}
		return false;
	}
		
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		Object nv = getNewValue();
		Object ov = getOldValue();
		result = prime * result + ((nv == null) ? 0 : nv.hashCode());
		result = prime * result + ((ov == null) ? 0 : ov.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer
			.append(getClass().getName())
			.append("{oldValue [") //$NON-NLS-1$
			.append(getOldValue() != null ? getOldValue().toString() : "null") //$NON-NLS-1$
			.append("], newValue [") //$NON-NLS-1$
			.append(getNewValue() != null ? getNewValue().toString() : "null") //$NON-NLS-1$
			.append("]}"); //$NON-NLS-1$
		
		return buffer.toString();
	}
}
