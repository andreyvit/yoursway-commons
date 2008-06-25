package com.yoursway.databinding;

/**
 * Used for wrapping objects that define their own implementations of equals()
 * and hashCode() when putting them in sets or hashmaps to ensure identity
 * comparison.
 * 
 * @since 1.0
 * 
 */
public class IdentityWrapper {
	final Object o;

	/**
	 * @param o
	 */
	public IdentityWrapper(Object o) {
		this.o = o;
	}
	
	/**
	 * @return the unwrapped object
	 */
	public Object unwrap() {
		return o;
	}

	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != IdentityWrapper.class) {
			return false;
		}
		return o == ((IdentityWrapper) obj).o;
	}

	public int hashCode() {
		return System.identityHashCode(o);
	}
}
