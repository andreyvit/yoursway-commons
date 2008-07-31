package com.yoursway.commons.preferences.core.tests;

import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

import com.yoursway.commons.preferences.core.PreferenceContainer;
import com.yoursway.commons.preferences.core.typed.StringPreferenceSetting;
import com.yoursway.utils.dependencies.DependentCodeRunner;

public class SimpleUpdatingTests {
    
    private PreferenceContainer container;
    private StringPreferenceSetting fooSetting;
    private StringWriter buf;
    private PrintWriter out;
    
    @Before
    public void setup() {
        buf = new StringWriter();
        out = new PrintWriter(buf);
        
        container = new PreferenceContainer();
        fooSetting = new StringPreferenceSetting(container, "foo");
    }
    
    void assertOutput(String string) {
        assertEquals(string.trim(), buf.toString().trim());
    }
    
    @Test
    public void clientsAreReexecutedWhenPreferenceIsUpdated() throws Exception {
        new DependentCodeRunner(new Runnable() {
            public void run() {
                out.println(fooSetting.value());
            }
        });
        container.update(new Runnable() {
            public void run() {
                fooSetting.setValue("bar");
                out.println("foo := bar");
            }
        });
        assertOutput("null\n" + "foo := bar\n" + "bar\n");
    }
    
    @Test
    public void readsAreConsistent() throws Exception {
        final Thread thread = new Thread() {
            public void run() {
                container.update(new Runnable() {
                    public void run() {
                        fooSetting.setValue("bar");
                        out.println("foo := bar");
                    }
                });
            }
        };
        new DependentCodeRunner(new Runnable() {
            
            private boolean done = false;
            
            public void run() {
                out.println(fooSetting.value());
                if (!done) {
                    thread.start();
                    done = true;
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                out.println(fooSetting.value());
            }
            
        });
        assertOutput("null\n" + "foo := bar\n" + "null\n" + "bar\n" + "bar\n");
    }
    
}
