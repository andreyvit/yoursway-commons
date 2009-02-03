package com.yoursway.commons.excelexport;

public enum Alignment {
    
    AUTO(null),
    
    LEFT("left"),
    
    CENTER("center"),
    
    RIGHT("right"),
    
    ;
    
    private final String xmlName;

    private Alignment(String xmlName) {
        this.xmlName = xmlName;
    }
    
    public String xmlName() {
        return xmlName;
    }
    
}
