package com.yoursway.commons.preferences.core.branch;

import com.yoursway.commons.preferences.core.layers.PreferenceLayer;

public interface PreferenceBranchListener {
    
    void updated(PreferenceLayer oldLayer, PreferenceLayer newLayer);
    
}
