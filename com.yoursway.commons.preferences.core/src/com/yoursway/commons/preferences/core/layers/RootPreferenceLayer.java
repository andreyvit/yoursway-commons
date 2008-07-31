package com.yoursway.commons.preferences.core.layers;

import java.util.Collections;
import java.util.Map;

public class RootPreferenceLayer extends PreferenceLayer {
    
    public static final RootPreferenceLayer INSTANCE = new RootPreferenceLayer();
    
    private RootPreferenceLayer() {
    }

    @Override
    public String get(String name) {
        return null;
    }

    @Override
    Map<String, String> values() {
        return Collections.emptyMap();
    }
    
}
