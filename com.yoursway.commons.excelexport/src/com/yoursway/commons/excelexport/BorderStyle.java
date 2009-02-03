package com.yoursway.commons.excelexport;

public enum BorderStyle {
    
    NONE(null),
    
    THIN("thin"),
    
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
