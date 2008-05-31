package com.yoursway.osgi.javaflow.tests.continuations;

import org.apache.commons.javaflow.Continuation;

public class Foo implements Runnable {
    
    public int stage;

    @Override
    public void run() {
        stage = 1;
        Continuation.suspend();
        stage = 2;
    }
    
}
