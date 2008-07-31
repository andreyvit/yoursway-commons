package com.yoursway.commons.preferences.core.layers;

import java.util.Map;

public abstract class PreferenceLayer {
    
    public abstract String get(String name);
    
    abstract Map<String, String> values();

    public boolean isMutable() {
        return false;
    }

    public void set(String name, String newValue) {
        throw new UnsupportedOperationException();
    }

}
