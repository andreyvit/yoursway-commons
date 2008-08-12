package com.yoursway.commons.preferences.core;

import static com.yoursway.utils.broadcaster.BroadcasterFactory.newBroadcaster;

import com.yoursway.utils.EventSource;
import com.yoursway.utils.broadcaster.Broadcaster;
import com.yoursway.utils.dependencies.Dependee;
import com.yoursway.utils.dependencies.DependeeListener;
import com.yoursway.utils.dependencies.Tracker;
import com.yoursway.utils.dependencies.TracksDependencies;

public abstract class PreferenceSetting implements Dependee, PreferenceContainerListener {
    
    private final PreferenceContainer container;
    private final String name;
    
    public PreferenceSetting(PreferenceContainer container, String name) {
        if (container == null)
            throw new NullPointerException("bag is null");
        if (name == null)
            throw new NullPointerException("name is null");
        this.container = container;
        this.name = name;
        container.events().addListener(this);
    }
    
    @TracksDependencies
    protected String rawValue() {
        Tracker.dependsOn(this);
        return container.get(name);
    }
    
    protected void setRawValue(String newValue) {
        container.set(name, newValue);
    }
    
    private transient Broadcaster<DependeeListener> broadcaster = newBroadcaster(DependeeListener.class);
    
    public EventSource<DependeeListener> dependeeEvents() {
        return broadcaster;
    }
    
    public void somethingChanged() {
        broadcaster.fire().changed(this);
    }
    
}
