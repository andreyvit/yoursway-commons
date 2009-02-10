package com.yoursway.commons.excelexport;

import java.io.IOException;

import com.yoursway.utils.XmlWriter;

public class Border {
    
    public static final Border NONE = new Border(BorderStyle.NONE, Color.AUTO); 
    
    public static final Border DOTTED = new Border(BorderStyle.DOTTED, Color.AUTO); 
    
    public static final Border THIN = new Border(BorderStyle.THIN, Color.AUTO); 
    
    public static final Border MEDIUM = new Border(BorderStyle.MEDIUM, Color.AUTO); 

    public static final Border THICK = new Border(BorderStyle.THICK, Color.AUTO); 
    
    private final BorderStyle style;
    private final Color color;
    
    public Border(BorderStyle style, Color color) {
        if (style == null)
            throw new NullPointerException("style is null");
        if (color == null)
            throw new NullPointerException("color is null");
        this.style = style;
        this.color = color;
    }
    
    void encode(XmlWriter xml) throws IOException {
        if (style == BorderStyle.NONE)
            return;
        xml.attr("style", style.xmlName());
        xml.start("color");
        color.encode(xml);
        xml.end();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + ((style == null) ? 0 : style.hashCode());
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
        Border other = (Border) obj;
        if (color == null) {
            if (other.color != null)
                return false;
        } else if (!color.equals(other.color))
            return false;
        if (style == null) {
            if (other.style != null)
                return false;
        } else if (!style.equals(other.style))
            return false;
        return true;
    }
    
}
