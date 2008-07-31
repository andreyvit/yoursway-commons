package com.yoursway.commons.preferences.core.layers;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;
import java.util.Map.Entry;

public class MutablePreferenceLayer extends PreferenceLayer {
    
    private Map<String, String> values = newHashMap();
    
    public MutablePreferenceLayer(PreferenceLayer parent) {
        values.putAll(parent.values());
    }

    public String get(String name) {
        return values.get(name);
    }

    public void set(String name, String newValue) {
        if (name == null)
            throw new NullPointerException("name is null");
        if (newValue == null)
            values.remove(name);
        else
            values.put(name, newValue);
    }

    public void update(Map<String, String> changes) {
        for (Entry<String, String> change : changes.entrySet())
            set(change.getKey(), change.getValue());
    }

    Map<String, String> values() {
        return values;
    }
    
    @Override
    public boolean isMutable() {
        return true;
    }

}
