package com.yoursway.commons.commitmodel.demo.properties.state;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;
import java.util.Map.Entry;

public class PropertyBagState {
    
    private Map<String, String> values = newHashMap();
    
    public PropertyBagState() {
    }
    
    public PropertyBagState(PropertyBagState parent) {
        values.putAll(parent.values);
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

    public PropertyBagState createChild() {
        return new PropertyBagState(this);
    }

    public void update(Map<String, String> changes) {
        for (Entry<String, String> change : changes.entrySet())
            set(change.getKey(), change.getValue());
    }
    
}

