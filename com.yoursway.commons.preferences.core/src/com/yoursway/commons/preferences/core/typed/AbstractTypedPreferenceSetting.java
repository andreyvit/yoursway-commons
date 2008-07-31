package com.yoursway.commons.preferences.core.typed;

import com.yoursway.commons.preferences.core.PreferenceContainer;
import com.yoursway.commons.preferences.core.PreferenceSetting;

public abstract class AbstractTypedPreferenceSetting<T> extends PreferenceSetting {

    public AbstractTypedPreferenceSetting(PreferenceContainer container, String name) {
        super(container, name);
    }
    
    public T value() {
        return decode(rawValue());
    }
    
    public void setValue(T newValue) {
        setRawValue(encode(newValue));
    }
    
    protected abstract T decode(String raw); 
    
    protected abstract String encode(T value); 
    
}
