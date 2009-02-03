package com.yoursway.commons.excelexport;

import java.util.EnumSet;

public enum Edge {
    
    LEFT("left"),
    
    RIGHT("right"),
    
    TOP("top"),
    
    BOTTOM("bottom"),
    
    DIAGONAL("diagonal"),
    
    ;
    
    public static final EnumSet<Edge> OUTER = EnumSet.of(LEFT, RIGHT, TOP, BOTTOM);
    
    private final String xmlName;
    
    private Edge(String xmlName) {
        this.xmlName = xmlName;
    }
    
    public String xmlName() {
        return xmlName;
    }
    
}
