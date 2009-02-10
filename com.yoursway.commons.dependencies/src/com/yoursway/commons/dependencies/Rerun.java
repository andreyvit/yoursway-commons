/**
 * 
 */
package com.yoursway.commons.dependencies;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.yoursway.commons.dependencies.internal.DependencyCollector;
import com.yoursway.utils.disposable.DisposableImpl;
import com.yoursway.utils.disposable.DisposedException;
import com.yoursway.utils.disposable.Disposer;

public abstract class Rerun extends DisposableImpl implements Observer,
		Runnable {
	private Map<Observable, Boolean> previousDependencies = new HashMap<Observable, Boolean>();

	protected Rerun(Disposer disposer) {
		super(disposer);
		execute();
	}

	public final void observedObjectDidChange() {
		execute();
	}

	private void execute() {
		for (Map.Entry<Observable, Boolean> entry : previousDependencies
				.entrySet())
			entry.setValue(Boolean.FALSE);
		try {
			Dependencies.run(this, new DependencyCollector() {

				public void dependency(Observable observable) {
					if (previousDependencies.put(observable, Boolean.TRUE) == null)
						EventBus.subscribe(observable, Rerun.this);
				}

			});
			for (Iterator<Map.Entry<Observable, Boolean>> iterator = previousDependencies
					.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<Observable, Boolean> entry = iterator.next();
				if (entry.getValue() == Boolean.FALSE) {
					EventBus.unsubscribe(entry.getKey(), Rerun.this);
					iterator.remove();
				}
			}
		} catch (DisposedException e) {
			dispose();
		}
	}

	@Override
	protected void disposeAdditionalResources() {
		for (Map.Entry<Observable, Boolean> entry : previousDependencies
				.entrySet())
			EventBus.unsubscribe(entry.getKey(), Rerun.this);
		previousDependencies.clear();
		super.disposeAdditionalResources();
	}

}
