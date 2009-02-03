package com.yoursway.commons.excelexport;

import java.io.IOException;

import com.yoursway.utils.XmlWriter;

public abstract class Fill {
    
    abstract void encode(XmlWriter xml) throws IOException;
    
    public static final Fill NONE = new Fill() {
        
        @Override
        public void encode(XmlWriter xml) throws IOException {
            xml.tag("patternFill", "patternType", "none");
        }
        
        public int hashCode() {
            return 1;
        };
        
        public boolean equals(Object obj) {
            return obj == this;
        };

    };
    
    public static final Fill GRAY125 = new Fill() {
        
        @Override
        public void encode(XmlWriter xml) throws IOException {
            xml.tag("patternFill", "patternType", "gray125");
        }
        
        public int hashCode() {
            return 2;
        };
        
        public boolean equals(Object obj) {
            return obj == this;
        };
 
    };
    
    public abstract int hashCode();
    
    public abstract boolean equals(Object obj);
    
}
