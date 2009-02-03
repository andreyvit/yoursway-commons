package com.yoursway.commons.excelexport;

import java.io.IOException;

import com.yoursway.utils.XmlWriter;

public abstract class Color {
    
    public abstract void encode(XmlWriter xml) throws IOException;
    
    public abstract int hashCode();
    
    public abstract boolean equals(Object obj);
    
    public static final Color AUTO = new Color() {
        
        public void encode(XmlWriter xml) throws IOException {
            xml.attr("auto", "1");
        }
        
        public boolean equals(Object obj) {
            return obj == this;
        }
        
        public int hashCode() {
            return 42;
        }
        
        public String toString() {
            return "AUTO";
        };
        
    };
    
}
