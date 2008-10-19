package com.yoursway.completion;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static Activator instance;
	private BundleContext context;

	public static Activator getInstance() {
		return instance;
	}
	
	public Bundle getBundle() {
		return context.getBundle();
	}
	
	public void start(BundleContext context) throws Exception {
		this.context = context;
		instance = this;
		
	}

	public void stop(BundleContext context) throws Exception {
		this.context = null;
		instance = null;
	}

}
