package com.yoursway.commons.dependencies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.yoursway.commons.dependencies.internal.AutoCollection;

public class UnorderedOwnedSetImpl<E extends IdentityObject> extends ValueObject {
	
	protected final Collection<E> items;
	
	private final Collection<E> unmodItems;

	public UnorderedOwnedSetImpl(IdentityObject owner) {
		super(owner);
		items = new AutoCollection<E>(owner, new ArrayList<E>());
		unmodItems = Collections.unmodifiableCollection(items);
	}
	
	public Collection<E> asCollection() {
		return unmodItems;
	}

}
