package com.yoursway.commons.excelexport;

import java.io.IOException;

import com.yoursway.utils.XmlWriter;

public class IndexedColor extends Color {
    
    private final int index;
    
    public static final IndexedColor INDEXED_64 = new IndexedColor(64);

    public IndexedColor(int index) {
        this.index = index;
    }

    public void encode(XmlWriter xml) throws IOException {
        xml.attr("indexed", "" + index);
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        IndexedColor other = (IndexedColor) obj;
        if (index != other.index)
            return false;
        return true;
    }
    
}
