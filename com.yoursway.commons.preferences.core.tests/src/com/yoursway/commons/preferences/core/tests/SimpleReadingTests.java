package com.yoursway.commons.preferences.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

import com.yoursway.commons.preferences.core.PreferenceContainer;
import com.yoursway.commons.preferences.core.layers.MutablePreferenceLayer;
import com.yoursway.commons.preferences.core.typed.StringPreferenceSetting;
import com.yoursway.utils.dependencies.DependentCodeRunner;

public class SimpleReadingTests {
    
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
    public void emptyInitially() throws Exception {
        new DependentCodeRunner(new Runnable() {
            
            public void run() {
                assertNull(fooSetting.value());
            }
            
        });
    }
    
    @Test
    public void clientsAreReexecutedWhenPreferenceIsUpdated() throws Exception {
        new DependentCodeRunner(new Runnable() {

            public void run() {
                out.println(fooSetting.value());
            }
            
        });
        MutablePreferenceLayer newLayer = new MutablePreferenceLayer(container.uiBranch.getLayer());
        newLayer.set("foo", "bar");
        container.uiBranch.update(newLayer);
        assertOutput("null\nbar\n");
    }
    
    @Test
    public void readsAreConsistent() throws Exception {
        new DependentCodeRunner(new Runnable() {
            
            private boolean done = false;
            
            public void run() {
                out.println(fooSetting.value());
                if (!done) {
                    done = true;
                    MutablePreferenceLayer newLayer = new MutablePreferenceLayer(container.uiBranch.getLayer());
                    newLayer.set("foo", "bar");
                    container.uiBranch.update(newLayer);
                }
                out.println(fooSetting.value());
            }
            
        });
        assertOutput("null\nnull\nbar\nbar\n");
    }
    
}
