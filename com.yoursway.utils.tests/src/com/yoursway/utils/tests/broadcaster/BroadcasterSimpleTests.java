package com.yoursway.utils.tests.broadcaster;

import static com.yoursway.utils.broadcaster.BroadcasterFactory.newBroadcaster;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.yoursway.utils.broadcaster.Broadcaster;
import com.yoursway.utils.bugs.Bugs;
import com.yoursway.utils.bugs.NopBugHandler;
import com.yoursway.utils.bugs.Severity;

public class BroadcasterSimpleTests {
    
    private Broadcaster<MyListener> broadcaster;
    private MyProducer producer;
    private int calls = 0;
    
    @Before
    public void createBroadcaster() {
        broadcaster = newBroadcaster(MyListener.class);
        producer = new MyProducer();
    }
    
    @Test
    public void canBeCalledWithoutListeners() throws Exception {
        broadcaster.fire().changed(producer);
    }
    
    @Test
    public void callsListener() throws Exception {
        MyListener myListener = new MyListener() {
            public void changed(MyProducer aProducer) {
                assertEquals(producer, aProducer);
                calls++;
            }
        };
        broadcaster.addListener(myListener);
        assertEquals(0, calls);
        broadcaster.fire().changed(producer);
        assertEquals(1, calls);
    }
    
    @Test
    public void callsTwoListeners() throws Exception {
        MyListener myListener = new MyListener() {
            public void changed(MyProducer aProducer) {
                assertEquals(producer, aProducer);
                calls++;
            }
        };
        MyListener myListener2 = new MyListener() {
            public void changed(MyProducer aProducer) {
                assertEquals(producer, aProducer);
                calls += 100;
            }
        };
        broadcaster.addListener(myListener);
        broadcaster.addListener(myListener2);
        assertEquals(0, calls);
        broadcaster.fire().changed(producer);
        assertEquals(101, calls);
    }
    
    @Test
    public void catchesExceptions() throws Exception {
        MyListener myListener = new MyListener() {
            public void changed(MyProducer aProducer) {
                throw new AssertionError("Mua-ha-ha");
            }
        };
        MyListener myListener2 = new MyListener() {
            public void changed(MyProducer aProducer) {
                assertEquals(producer, aProducer);
                calls += 42;
            }
        };
        broadcaster.addListener(myListener);
        broadcaster.addListener(myListener2);
        Bugs.setHandler(new NopBugHandler() {
            public void error(Severity severity, Throwable error, String details) {
                calls += 24;
            }
        });
        assertEquals(0, calls);
        broadcaster.fire().changed(producer);
        assertEquals(42 + 24, calls);
    }
    
}
