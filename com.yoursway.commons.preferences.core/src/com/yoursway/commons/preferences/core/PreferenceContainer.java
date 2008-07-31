package com.yoursway.commons.preferences.core;

import static com.yoursway.utils.broadcaster.BroadcasterFactory.newBroadcaster;

import com.yoursway.commons.preferences.core.branch.PreferenceBranch;
import com.yoursway.commons.preferences.core.branch.PreferenceBranchListener;
import com.yoursway.commons.preferences.core.layers.MutablePreferenceLayer;
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
    
    void set(String name, String value) {
        PreferenceLayer layer = layerSlot.get();
        if (layer == null)
            throw new IllegalStateException("You can only update a preference from a runnable passed to PreferenceContainer.update() method");
        if (!layer.isMutable())
            throw new IllegalStateException("You cannot update preferences from within a tracking section, please see PreferenceContainer.update() method");
        layer.set(name, value);
    }
    
    public void update(Runnable runnable) {
        PreferenceLayer layer = layerSlot.get();
        if (layer != null)
            throw new IllegalStateException("Cannot start preference update from within a tracking section");
        
        final MutablePreferenceLayer newLayer = new MutablePreferenceLayer(uiBranch.getLayer());
        layerSlot.set(newLayer);
        Tracker.runWithFinishedListener(runnable, new TrackingSectionListener() {

            public void trackingFinished() {
                layerSlot.set(null);
                uiBranch.update(newLayer);
            }
            
        });
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
 
}
