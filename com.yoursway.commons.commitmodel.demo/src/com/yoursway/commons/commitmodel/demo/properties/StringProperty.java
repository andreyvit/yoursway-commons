package com.yoursway.commons.commitmodel.demo.properties;

public class StringProperty extends Property {

    public StringProperty(PropertyBag bag, String name) {
        super(bag, name);
    }
    
    public String value() {
        return rawValue();
    }
    
    public void update(String value, PropertyBagUpdateSession session) {
        rawUpdate(value, session);
    }

}
