package com.yoursway.commons.dependencies;

import com.yoursway.utils.disposable.Disposable;
import com.yoursway.utils.disposable.Disposer;
import com.yoursway.utils.disposable.UndoableDisposer;

public class ValueObject extends Object implements Disposer {

	private final IdentityObject owner;

	public ValueObject(IdentityObject owner) {
		if (owner == null)
			throw new NullPointerException("owner is null");
		this.owner = owner;
	}

	public UndoableDisposer alsoDispose(Disposable disposable) {
		return owner.alsoDispose(disposable);
	}

}
