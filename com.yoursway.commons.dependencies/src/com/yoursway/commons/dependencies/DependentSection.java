/**
 * 
 */
package com.yoursway.commons.dependencies;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

import com.yoursway.commons.dependencies.internal.DependencyCollector;
import com.yoursway.utils.disposable.DisposableImpl;
import com.yoursway.utils.disposable.DisposedException;
import com.yoursway.utils.disposable.Disposer;

public abstract class DependentSection extends DisposableImpl implements
		Observer, Runnable {
	private Map<Mutable, Boolean> previousDependencies = new IdentityHashMap<Mutable, Boolean>();

	protected DependentSection(Disposer disposer) {
		super(disposer);
		execute();
	}

	public final void observedObjectDidChange() {
		execute();
	}

	private void execute() {
		for (Map.Entry<Mutable, Boolean> entry : previousDependencies
				.entrySet())
			entry.setValue(Boolean.FALSE);
		try {
			Dependencies.run(this, new DependencyCollector() {

				public void dependency(Mutable observable) {
					if (previousDependencies.put(observable, Boolean.TRUE) == null)
						EventBus.subscribe(observable, DependentSection.this);
				}

			});
			for (Iterator<Map.Entry<Mutable, Boolean>> iterator = previousDependencies
					.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<Mutable, Boolean> entry = iterator.next();
				if (entry.getValue() == Boolean.FALSE) {
					EventBus.unsubscribe(entry.getKey(), DependentSection.this);
					iterator.remove();
				}
			}
		} catch (DisposedException e) {
			dispose();
		}
	}

	@Override
	protected void disposeAdditionalResources() {
		for (Map.Entry<Mutable, Boolean> entry : previousDependencies
				.entrySet())
			EventBus.unsubscribe(entry.getKey(), DependentSection.this);
		previousDependencies.clear();
		super.disposeAdditionalResources();
	}

}
