package com.yoursway.utils.dependencies;

import com.yoursway.utils.EventSource;

public interface Dependee {
    
    EventSource<DependeeListener> dependeeEvents();
    
}
