package com.yoursway.commons.dependencies.internal;

import com.yoursway.commons.dependencies.Mutable;

public interface DependencyCollector {
	
	void dependency(Mutable observable);

}
