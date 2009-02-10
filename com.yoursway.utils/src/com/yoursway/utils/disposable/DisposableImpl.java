package com.yoursway.utils.disposable;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.synchronizedCollection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.yoursway.utils.bugs.Bugs;

public abstract class DisposableImpl implements Disposable, Disposer,
		UndoableDisposer {

	private volatile boolean disposed = false;

	private final Collection<UndoableDisposer> parents;

	private final Collection<Disposable> disposables = synchronizedCollection(Lists
			.<Disposable> newArrayList());

	public DisposableImpl(Disposer parent) {
		this(Collections.singleton(parent));
	}

	public DisposableImpl(Collection<Disposer> parents) {
		this.parents = new ArrayList<UndoableDisposer>(parents.size());
		for (Disposer parent : parents)
			this.parents.add(parent.alsoDispose(this));
	}

	public final void dispose() {
		synchronized (this) {
			if (disposed)
				return;
			disposed = true;
		}
		disposeAdditionalResources();
		disposeAnnotatedFieldChildren();
		disposeRegisteredDisposables();
		deliverDisposedNotification();
		for (UndoableDisposer parent : parents)
			parent.abstainFromDisposing(this);
	}

	private final void disposeAnnotatedFieldChildren() {
		for (Class<?> klass = getClass(); klass != null; klass = klass
				.getSuperclass()) {
			for (Field field : klass.getDeclaredFields()) {
				if (!Modifier.isStatic(field.getModifiers())
						&& field.getAnnotation(DisposableChild.class) != null) {
					field.setAccessible(true);
					try {
						Object disposable = field.get(this);
						if (disposable instanceof Disposable)
							disposeChild((Disposable) disposable);
						else if (disposable != null) {
							Method method = disposable.getClass().getMethod(
									"dispose");
							try {
								method.invoke(disposable);
							} catch (Throwable e) {
								Bugs.listenerFailed(e, disposable, "dispose");
							}
						}
					} catch (ClassCastException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						String fieldName = getClass().getName() + "."
								+ field.getName();
						throw new AssertionError(
								"Field marked with @DisposableChild does not have a public void dispose() method: "
										+ fieldName);
					}
				}
			}
		}
	}

	public final boolean isDisposed() {
		return disposed;
	}

	protected void disposeAdditionalResources() {
	}

	protected void deliverDisposedNotification() {
	}

	final void disposeChild(Disposable disposable) {
		try {
			disposable.dispose();
		} catch (Throwable e) {
			Bugs.listenerFailed(e, disposable, "dispose");
		}
	}

	protected void checkAccess() {
		if (disposed)
			throw new DisposedException();
	}

	private final void disposeRegisteredDisposables() {
		List<Disposable> list = newArrayList(disposables);
		for (Disposable disposable : list)
			disposeChild(disposable);
		disposables.clear();
	}

	public final UndoableDisposer alsoDispose(Disposable disposable) {
		if (disposable == null)
			throw new NullPointerException("disposable is null");
		synchronized (this) {
			if (!isDisposed()) {
				disposables.add(disposable);
				return this;
			}
		}
		disposeChild(disposable);
		return this;
	}

	public final void abstainFromDisposing(Disposable disposable) {
		if (disposable == null)
			throw new NullPointerException("disposable is null");
		synchronized (this) {
			if (isDisposed())
				return;
			disposables.remove(disposable);
		}
	}

}
