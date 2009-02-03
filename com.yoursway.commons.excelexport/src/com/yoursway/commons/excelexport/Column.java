package com.yoursway.commons.excelexport;

import java.io.IOException;

import com.yoursway.utils.XmlWriter;

public class Column {
    
    private final int ordinal;
    
    private int width;
    
    private boolean widthSet;

    private final Sheet sheet;

    public Column(Sheet sheet, int ordinal) {
        if (sheet == null)
            throw new NullPointerException("sheet is null");
        this.sheet = sheet;
        this.ordinal = ordinal;
    }
    
    public int ordinal() {
        return ordinal;
    }
    
    public Column width(int widthInChars) {
        width = widthInChars;
        widthSet = true;
        return this;
    }
    
    public boolean shouldEncode() {
        return widthSet;
    }
    
    public void encode(XmlWriter xml) throws IOException {
        xml.start("col", "min", "" + ordinal, "max", "" + ordinal);
        if (widthSet)
            xml.attr("width", "" + width).attr("customWidth", "1");
        xml.end();
    }
    
    public Range range() {
        return sheet.range().reduceToColumn(this);
    }
    
    public Column next() {
        return sheet.column(ordinal + 1);
    }

}
