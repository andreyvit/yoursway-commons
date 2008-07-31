package com.yoursway.commons.preferences.core;

import static com.yoursway.utils.broadcaster.BroadcasterFactory.newBroadcaster;

import com.yoursway.commons.preferences.core.branch.PreferenceBranch;
import com.yoursway.commons.preferences.core.branch.PreferenceBranchListener;
import com.yoursway.commons.preferences.core.layers.PreferenceLayer;
import com.yoursway.utils.EventSource;
import com.yoursway.utils.broadcaster.Broadcaster;

public class PreferenceContainer {
    
    public final PreferenceBranch uiBranch = new PreferenceBranch();
    
    public PreferenceContainer() {
        uiBranch.events().addListener(new PreferenceBranchListener() {

            public void updated(PreferenceLayer oldLayer, PreferenceLayer newLayer) {
                broadcaster.fire().somethingChanged();
            }
            
        });
    }
    
    public String get(String name) {
        return uiBranch.get(name);
    }
    
    private final Broadcaster<PreferenceContainerListener> broadcaster = newBroadcaster(PreferenceContainerListener.class);
    
    public final EventSource<PreferenceContainerListener> events() {
        return broadcaster;
    }
 
}
