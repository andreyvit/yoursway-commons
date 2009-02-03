package com.yoursway.commons.excelexport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.yoursway.utils.XmlWriter;

public class BorderSet {
    
    public static final BorderSet DEFAULT = new BorderSet(createDefaultBorders());
    
    private List<Border> borders;
    
    private BorderSet(List<Border> borders) {
        this.borders = borders;
    }
    
    private static List<Border> createDefaultBorders() {
        int length = Edge.values().length;
        List<Border> result = new ArrayList<Border>(length);
        for (int i = 0; i < length; i++)
            result.add(Border.NONE);
        return result;
    }
    
    public BorderSet with(Border border, EnumSet<Edge> edges) {
        List<Border> newBorders = new ArrayList<Border>(borders);
        for (Edge edge : edges)
            newBorders.set(edge.ordinal(), border);
        return new BorderSet(newBorders);
    }
    
    public void encode(XmlWriter xml) throws IOException {
        for (Edge edge : Edge.values()) {
            xml.start(edge.xmlName());
            borders.get(edge.ordinal()).encode(xml);
            xml.end();
        }
    }
    
}
