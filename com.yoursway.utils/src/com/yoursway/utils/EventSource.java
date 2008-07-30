package com.yoursway.utils;

public interface EventSource<Listener> {
    
    void addListener(Listener listener);
    
    void removeListener(Listener listener);
    
}
