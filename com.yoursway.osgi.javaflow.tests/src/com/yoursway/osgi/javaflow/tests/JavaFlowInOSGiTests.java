package com.yoursway.osgi.javaflow.tests;

import static org.junit.Assert.*;

import org.apache.commons.javaflow.Continuation;
import org.junit.Test;

import com.yoursway.osgi.javaflow.tests.continuations.Foo;

public class JavaFlowInOSGiTests {
    
    @Test
    public void javaFlowWorksGreatInEquinoxThanksToAdaptorHooks() throws Exception {
        Foo foo = new Foo();
        assertEquals(0, foo.stage);
        Continuation c = Continuation.startWith(foo);
        assertEquals(1, foo.stage);
        Continuation.continueWith(c);
        assertEquals(2, foo.stage);
    }
    
}
