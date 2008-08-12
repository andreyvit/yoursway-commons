package com.yoursway.commons.commitmodel.demo.properties;

import static com.yoursway.utils.broadcaster.BroadcasterFactory.newBroadcaster;

import com.yoursway.commons.commitmodel.demo.properties.state.PropertyBagState;
import com.yoursway.utils.EventSource;
import com.yoursway.utils.broadcaster.Broadcaster;

public class PropertyBag {
    
    private volatile PropertyBagState state;
    
    public PropertyBag(PropertyBagState initialState) {
        if (initialState == null)
            throw new NullPointerException("initialState is null");
        this.state = initialState;
    }
    
    public PropertyBagState getState() {
        return state;
    }
    
    public void setState(PropertyBagState state) {
        if (state == null)
            throw new NullPointerException("state is null");
        this.state = state;
        broadcaster.fire().somethingChanged();
    }
    
    public String get(String name) {
        return state.get(name);
    }
    
    private final Broadcaster<PropertyBagListener> broadcaster = newBroadcaster(PropertyBagListener.class);
    
    public final EventSource<PropertyBagListener> events() {
        return broadcaster;
    }
    
}
