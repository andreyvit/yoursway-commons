package com.yoursway.commons.excelexport;

public enum BorderStyle {
    
    NONE(null),
    
    DOTTED("dotted"),
    
    DASHED("hair"),
    
    THIN("thin"),
    
    MEDIUM("medium"),
    
    THICK("thick"),
    
    ; 
    
    private final String xmlName;

    private BorderStyle(String xmlName) {
        this.xmlName = xmlName;
    }
    
    public String xmlName() {
        return xmlName;
    }
    
}
