package com.yoursway.databinding;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.widgets.Display;

/**
 * A factory for creating observables for SWT widgets
 * 
 * @since 1.1
 * 
 */
public class SWTObservables {

	private static java.util.List<DisplayRealm> realms = new ArrayList<DisplayRealm>();

	/**
	 * Returns the realm representing the UI thread for the given display.
	 * 
	 * @param display
	 * @return the realm representing the UI thread for the given display
	 */
	public static Realm getRealm(final Display display) {
		synchronized (realms) {
			for (Iterator<DisplayRealm> it = realms.iterator(); it.hasNext();) {
				DisplayRealm displayRealm = it.next();
				if (displayRealm.display == display) {
					return displayRealm;
				}
			}
			DisplayRealm result = new DisplayRealm(display);
			realms.add(result);
			return result;
		}
	}

	private static class DisplayRealm extends Realm {
		private Display display;

		/**
		 * @param display
		 */
		private DisplayRealm(Display display) {
			this.display = display;
		}

		public boolean isCurrent() {
			return Display.getCurrent() == display;
		}

		public void asyncExec(final Runnable runnable) {
			Runnable safeRunnable = new Runnable() {
				public void run() {
					safeRun(runnable);
				}
			};
			if (!display.isDisposed()) {
				display.asyncExec(safeRunnable);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return (display == null) ? 0 : display.hashCode();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final DisplayRealm other = (DisplayRealm) obj;
			if (display == null) {
				if (other.display != null)
					return false;
			} else if (!display.equals(other.display))
				return false;
			return true;
		}
	}
	
}
