package com.yoursway.commons.preferences.core.typed;

import com.yoursway.commons.preferences.core.PreferenceContainer;

public class StringPreferenceSetting extends AbstractTypedPreferenceSetting<String> {

    public StringPreferenceSetting(PreferenceContainer container, String name) {
        super(container, name);
    }

    protected String decode(String raw) {
        return raw;
    }
    
}
