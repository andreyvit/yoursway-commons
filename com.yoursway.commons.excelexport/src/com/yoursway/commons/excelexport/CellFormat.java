package com.yoursway.commons.excelexport;

import java.io.IOException;
import java.util.EnumSet;

import com.yoursway.utils.XmlWriter;

public class CellFormat {
    
    public static final CellFormat DEFAULT = new CellFormat(Fill.NONE, Alignment.LEFT, BorderSet.DEFAULT);
    
    private final Fill fill;
    
    private final Alignment alignment;
    
    private final BorderSet borderSet;
    
    public CellFormat(Fill fill, Alignment alignment, BorderSet borderSet) {
        if (fill == null)
            throw new NullPointerException("fill is null");
        if (alignment == null)
            throw new NullPointerException("alignment is null");
        if (borderSet == null)
            throw new NullPointerException("borderSet is null");
        this.fill = fill;
        this.alignment = alignment;
        this.borderSet = borderSet;
    }
    
    public Fill fill() {
        return fill;
    }
    
    public BorderSet borderSet() {
        return borderSet;
    }
    
    public CellFormat with(Fill fill) {
        return new CellFormat(fill, alignment, borderSet);
    }
    
    public CellFormat with(Alignment alignment) {
        return new CellFormat(fill, alignment, borderSet);
    }
    
    public CellFormat with(BorderSet borderSet) {
        return new CellFormat(fill, alignment, borderSet);
    }
    
    public CellFormat with(Border border, EnumSet<Edge> edges) {
        return new CellFormat(fill, alignment, borderSet.with(border, edges));
    }
    
    public void encode(XmlWriter xml) throws IOException {
        if (!fill.equals(Fill.NONE))
            xml.attr("applyFill", "1");
        if (!borderSet.equals(BorderSet.DEFAULT))
            xml.attr("applyBorder", "1");
        if (alignment != Alignment.AUTO)
            xml.attr("applyAlignment", "1");
        if (alignment != Alignment.AUTO)
            xml.tag("alignment", "horizontal", alignment.xmlName());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alignment == null) ? 0 : alignment.hashCode());
        result = prime * result + ((borderSet == null) ? 0 : borderSet.hashCode());
        result = prime * result + ((fill == null) ? 0 : fill.hashCode());
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
        CellFormat other = (CellFormat) obj;
        if (alignment == null) {
            if (other.alignment != null)
                return false;
        } else if (!alignment.equals(other.alignment))
            return false;
        if (borderSet == null) {
            if (other.borderSet != null)
                return false;
        } else if (!borderSet.equals(other.borderSet))
            return false;
        if (fill == null) {
            if (other.fill != null)
                return false;
        } else if (!fill.equals(other.fill))
            return false;
        return true;
    }
    
}
