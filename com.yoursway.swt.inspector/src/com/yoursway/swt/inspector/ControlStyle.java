package com.yoursway.swt.inspector;

public class ControlStyle {
    
    private final int value;
    private final String name;

    public ControlStyle(int value, String name) {
        this.value = value;
        this.name = name;
    }
    
    public boolean isSet(int style) {
        if (value == 0)
            return false;
        return (style & value) == value;
    }
    
    public String getName() {
        return name;
    }
    
}