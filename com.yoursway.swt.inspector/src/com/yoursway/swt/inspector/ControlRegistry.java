package com.yoursway.swt.inspector;

import static com.google.common.collect.Maps.newHashMap;

import java.lang.reflect.Field;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

public class ControlRegistry {
    
    private Map<Class<? extends Control>, ControlDescriptor> descriptors = newHashMap();
    
    void register(Class<? extends Control> klass, String styles) {
        ControlDescriptor descriptor = lookup(klass);
        Class<SWT> swt = SWT.class;
        if (styles.length() == 0)
            descriptor.noStyles();
        else
            for (String name : styles.split(",")) {
                name = name.trim();
                try {
                    Field field = swt.getDeclaredField(name);
                    int value = (Integer) field.get(null);
                    descriptor.addStyle(value, name);
                } catch (Exception e) {
                    throw new AssertionError(e);
                }
            }
    }

    public ControlDescriptor lookup(Class<? extends Control> klass) {
        ControlDescriptor result = descriptors.get(klass);
        if (result == null) {
            result = createDescriptor(klass);
            descriptors.put(klass, result);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private ControlDescriptor createDescriptor(Class<? extends Control> klass) {
        ControlDescriptor baseDescriptor; 
        if (klass.equals(Control.class))
            baseDescriptor = null;
        else
            baseDescriptor = lookup((Class<? extends Control>) klass.getSuperclass());
        return new ControlDescriptor(klass, baseDescriptor);
    }
    
}