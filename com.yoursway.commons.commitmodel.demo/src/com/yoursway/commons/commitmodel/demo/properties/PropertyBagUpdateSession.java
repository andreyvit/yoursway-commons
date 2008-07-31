package com.yoursway.commons.commitmodel.demo.properties;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import com.yoursway.commons.commitmodel.User;

public class PropertyBagUpdateSession {
    
    private final PropertyBag bag;
    
    private final Map<String, String> changes = newHashMap();

    public PropertyBagUpdateSession(PropertyBag bag) {
        if (bag == null)
            throw new NullPointerException("bag is null");
        this.bag = bag;
    }
    
    public void put(String name, String value) {
        if (name == null)
            throw new NullPointerException("name is null");
        changes.put(name, value);
    }
    
    public void commit(User user) {
        PropertyBagState state = bag.getState().createChild();
        state.update(changes);
        bag.setState(state);
    }
    
}
