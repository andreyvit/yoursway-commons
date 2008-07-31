package com.yoursway.commons.commitmodel.demo.properties;

import static com.yoursway.utils.broadcaster.BroadcasterFactory.newBroadcaster;

import com.yoursway.utils.EventSource;
import com.yoursway.utils.broadcaster.Broadcaster;
import com.yoursway.utils.dependencies.Dependee;
import com.yoursway.utils.dependencies.DependeeListener;
import com.yoursway.utils.dependencies.Tracker;
import com.yoursway.utils.dependencies.TracksDependencies;

public abstract class Property implements Dependee, PropertyBagListener {
    
    private final PropertyBag bag;
    private final String name;
    
    public Property(PropertyBag bag, String name) {
        if (bag == null)
            throw new NullPointerException("bag is null");
        if (name == null)
            throw new NullPointerException("name is null");
        this.bag = bag;
        this.name = name;
        bag.events().addListener(this);
    }
    
    @TracksDependencies
    protected String rawValue() {
        Tracker.dependsOn(this);
        return bag.get(name);
    }
    
    protected void rawUpdate(String rawValue, PropertyBagUpdateSession session) {
        session.put(name, rawValue);
    }
    
    private transient Broadcaster<DependeeListener> broadcaster = newBroadcaster(DependeeListener.class);
    
    public EventSource<DependeeListener> dependeeEvents() {
        return broadcaster;
    }
    
    public void somethingChanged() {
        broadcaster.fire().changed(this);
    }
    
}
