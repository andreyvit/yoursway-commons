package com.yoursway.commons.excelexport;

import java.io.IOException;

import com.yoursway.utils.XmlWriter;

public class SolidColorFill extends Fill {
    
    private final Color bgColor;

    public SolidColorFill(Color bgColor) {
        this.bgColor = bgColor;
    }

    @Override
    void encode(XmlWriter xml) throws IOException {
        xml.start("patternFill", "patternType", "solid");
        xml.start("fgColor");
        bgColor.encode(xml);
        xml.end();
        xml.start("bgColor");
        bgColor.encode(xml);
        xml.end();
        xml.end();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bgColor == null) ? 0 : bgColor.hashCode());
//        result = prime * result + ((fgColor == null) ? 0 : fgColor.hashCode());
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
        SolidColorFill other = (SolidColorFill) obj;
        if (bgColor == null) {
            if (other.bgColor != null)
                return false;
        } else if (!bgColor.equals(other.bgColor))
            return false;
//        if (fgColor == null) {
//            if (other.fgColor != null)
//                return false;
//        } else if (!fgColor.equals(other.fgColor))
//            return false;
        return true;
    }
    
}
