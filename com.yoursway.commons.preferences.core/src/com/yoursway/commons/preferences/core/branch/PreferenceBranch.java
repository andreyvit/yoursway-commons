package com.yoursway.commons.preferences.core.branch;

import static com.yoursway.utils.broadcaster.BroadcasterFactory.newBroadcaster;

import com.yoursway.commons.preferences.core.layers.PreferenceLayer;
import com.yoursway.commons.preferences.core.layers.RootPreferenceLayer;
import com.yoursway.utils.EventSource;
import com.yoursway.utils.annotations.SynchronizationNeededButUndecidedYet;
import com.yoursway.utils.broadcaster.Broadcaster;

@SynchronizationNeededButUndecidedYet
public class PreferenceBranch {
    
    private PreferenceLayer layer = RootPreferenceLayer.INSTANCE;
    
    public PreferenceBranch() {
    }
    
    public String get(String name) {
        return layer.get(name);
    }
    
    public PreferenceLayer getLayer() {
        return layer;
    }
    
    public void update(PreferenceLayer layer) {
        if (layer == null)
            throw new NullPointerException("layer is null");
        PreferenceLayer oldLayer = this.layer;
        this.layer = layer;
        broadcaster.fire().updated(oldLayer, layer);
    }
    
    private final Broadcaster<PreferenceBranchListener> broadcaster = newBroadcaster(PreferenceBranchListener.class);
    
    public final EventSource<PreferenceBranchListener> events() {
        return broadcaster;
    }

}
