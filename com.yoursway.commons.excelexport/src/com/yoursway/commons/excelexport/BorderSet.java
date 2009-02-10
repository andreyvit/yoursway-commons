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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((borders == null) ? 0 : borders.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BorderSet other = (BorderSet) obj;
        if (borders == null) {
            if (other.borders != null)
                return false;
        } else if (!borders.equals(other.borders))
            return false;
        return true;
    }
    
}
