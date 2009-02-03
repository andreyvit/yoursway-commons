package com.yoursway.commons.excelexport;

import java.io.IOException;

import com.yoursway.utils.XmlWriter;

public final class RGBColor extends Color {
    
    private final int r;
    private final int g;
    private final int b;
    
    public RGBColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    private String toHexString() {
        return hex(r) + hex(g) + hex(b);
    }
    
    private static String hex(int n) {
        String result = Integer.toHexString(n).toUpperCase();
        if (result.length() == 1)
            return "0" + result;
        else
            return result;
    }

    public void encode(XmlWriter xml) throws IOException {
        xml.attr("rgb", toHexString());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + b;
        result = prime * result + g;
        result = prime * result + r;
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
        RGBColor other = (RGBColor) obj;
        if (b != other.b)
            return false;
        if (g != other.g)
            return false;
        if (r != other.r)
            return false;
        return true;
    }
    
}
