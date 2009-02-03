package com.yoursway.commons.excelexport;

import java.io.IOException;

import com.yoursway.utils.XmlWriter;

public class Border {
    
    public static final Border NONE = new Border(BorderStyle.NONE, Color.AUTO); 
    
    public static final Border THIN = new Border(BorderStyle.THIN, Color.AUTO); 
    
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
    
}
