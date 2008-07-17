package com.yoursway.swt.inspector;

import static com.google.common.collect.Lists.newArrayList;
import static com.yoursway.utils.DebugOutputHelper.simpleNameOf;

import java.util.Collection;

import org.eclipse.swt.widgets.Control;


public class ControlDescriptor {
    
    private Collection<ControlStyle> styles = newArrayList();
    private final Class<? extends Control> klass;
    private final ControlDescriptor baseDescriptor;
    private boolean stylesKnown = false;
    
    public ControlDescriptor(Class<? extends Control> klass, ControlDescriptor baseDescriptor) {
        this.klass = klass;
        this.baseDescriptor = baseDescriptor;
    }
    
    public String className() {
        return simpleNameOf(klass);
    }
    
    public boolean isGenericControl() {
        return klassIs(Control.class);
    }
    
    public ControlDescriptor baseDescriptor() {
        return baseDescriptor;
    }
    
    public String name(Control control) {
        return className();
    }
    
    public void noStyles() {
        stylesKnown = true;
    }

    public void addStyle(int value, String name) {
        if (name == null)
            throw new NullPointerException("style is null");
        stylesKnown = true;
        styles.add(new ControlStyle(value, name));
    }
    
    public boolean klassIs(Class<? extends Control> klass) {
        return this.klass.equals(klass);
    }
    
    public Collection<String> stylesOf(Control control) {
        return stylesOf(control.getStyle());
    }
    
    public Collection<String> stylesOf(int styleValue) {
        Collection<String> result = newArrayList();
        for (ControlStyle style : styles)
            if (style.isSet(styleValue))
                result.add(style.getName());
        return result;
    }
    
    public boolean stylesKnown() {
        return stylesKnown;
    }
    
}