package com.yoursway.utils.broadcaster;

import com.yoursway.utils.EventSource;

public interface Broadcaster<Listener> extends EventSource<Listener> {
    
    Listener fire();
    
}
