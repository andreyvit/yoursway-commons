package com.yoursway.commons.dependencies;

import java.util.Collection;

import com.yoursway.utils.disposable.DisposableImpl;

public class IdentityObjectImpl extends DisposableImpl implements IdentityObject {

	public IdentityObjectImpl(Collection<? extends IdentityObject> parents) {
		super(parents);
	}

	public IdentityObjectImpl(IdentityObject... parents) {
		super(parents);
	}

	public IdentityObjectImpl(IdentityObject parent) {
		super(parent);
	}

}
