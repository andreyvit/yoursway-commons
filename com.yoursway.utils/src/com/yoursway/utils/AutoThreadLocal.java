package com.yoursway.utils;

public class AutoThreadLocal<T> {
    
    private ThreadLocal<T> storage = new ThreadLocal<T>();
    
    public T get() {
        return storage.get();
    }
    
    public void runWith(Runnable runnable, T newValue) {
        if (runnable == null)
            throw new NullPointerException("runnable is null");
        T oldValue = storage.get();
        storage.set(newValue);
        try {
            runnable.run();
        } finally {
            storage.set(oldValue);
        }
    }
    
    public static <T> AutoThreadLocal<T> create() {
        return new AutoThreadLocal<T>();
    }
    
}
