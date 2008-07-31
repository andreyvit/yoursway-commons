package com.yoursway.commons.preferences.core;

import static com.yoursway.utils.broadcaster.BroadcasterFactory.newBroadcaster;

import com.yoursway.commons.preferences.core.branch.PreferenceBranch;
import com.yoursway.commons.preferences.core.branch.PreferenceBranchListener;
import com.yoursway.commons.preferences.core.layers.PreferenceLayer;
import com.yoursway.utils.EventSource;
import com.yoursway.utils.broadcaster.Broadcaster;
import com.yoursway.utils.dependencies.Tracker;
import com.yoursway.utils.dependencies.TrackingSectionListener;

public class PreferenceContainer {
    
    public final PreferenceBranch uiBranch = new PreferenceBranch();
    
    public final ThreadLocal<PreferenceLayer> layerSlot = new ThreadLocal<PreferenceLayer>(); 
    
    public PreferenceContainer() {
        uiBranch.events().addListener(new PreferenceBranchListener() {

            public void updated(PreferenceLayer oldLayer, PreferenceLayer newLayer) {
                broadcaster.fire().somethingChanged();
            }
            
        });
    }
    
    String get(String name) {
        PreferenceLayer layer = lockReading();
        return layer.get(name);
//        return uiBranch.get(name);
    }
    
    private PreferenceLayer lockReading() {
        PreferenceLayer layer = layerSlot.get();
        if (layer == null) {
            layer = uiBranch.getLayer();
            layerSlot.set(layer);
            Tracker.addFinishListener(new TrackingSectionListener() {

                public void trackingFinished() {
                    layerSlot.set(null);
                }
                
            });
        }
        return layer;
    }

    private final Broadcaster<PreferenceContainerListener> broadcaster = newBroadcaster(PreferenceContainerListener.class);
    
    public final EventSource<PreferenceContainerListener> events() {
        return broadcaster;
    }
    
    public void update(Runnable runnable) {
        
    }
 
}
