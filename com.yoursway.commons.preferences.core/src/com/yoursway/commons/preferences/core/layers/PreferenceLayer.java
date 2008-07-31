package com.yoursway.commons.preferences.core.layers;

import java.util.Map;

public abstract class PreferenceLayer {
    
    public abstract String get(String name);
    
    abstract Map<String, String> values();

}
