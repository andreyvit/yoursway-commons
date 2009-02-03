package com.yoursway.commons.excelexport;

import java.util.EnumSet;

public class Cell {
    
    private final Row row;
    
    private Content content;
    
    private int horizonalSpan = 1;
    
    private CellFormat format = CellFormat.DEFAULT;

    private final Column column;
    
    public Cell(Row row, Column column) {
        this.row = row;
        this.column = column;
    }
    
    public int ordinal() {
        return column.ordinal();
    }
    
    public int horizonalSpan() {
        return horizonalSpan;
    }
    
    public Range hspan(int horizonalSpan) {
        this.horizonalSpan = horizonalSpan;
        for (int ordinal = this.ordinal() + 1; ordinal < this.ordinal() + horizonalSpan; ordinal++)
            row.cell(ordinal).setJoinedCell();
        return span();
    }
    
    public Range span() {
        return new Range(this, row.cell(ordinal() + horizonalSpan - 1));
    }
    
    public String colName() {
        int index = ordinal() - 1;
        if (index < 26)
            return letterWithIndex(index);
        else
            return letterWithIndex((index / 26) - 1) + letterWithIndex(index % 26);
    }
    
    private static String letterWithIndex(int index) {
        return Character.toString((char) ('A' + index));
    }
    
    public String name() {
        return colName() + row.ordinal();
    }
    
    public Cell text(String text) {
        willSetContent();
        this.content = new StringContent(text);
        return this;
    }
    
    public void acceptContentVisitor(ContentVisitor visitor) {
        if (content == null)
            visitor.visitNullContent();
        else
            content.accept(visitor);
    }
    
    public CellFormat format() {
        return format;
    }
    
    public Cell border(Border border, EnumSet<Edge> edges) {
        format = format.with(border, edges);
        return this;
    }
    
    public Cell border(Border border, Edge edge) {
        return border(border, EnumSet.of(edge));
    }
    
    public Cell fill(Fill fill) {
        span().fill(fill);
        return this;
    }
    
    Cell _fill(Fill fill) {
        if (fill == null)
            throw new NullPointerException("fill is null");
        format = format.with(fill);
        return this;
    }
    
    public Cell align(Alignment alignment) {
        if (alignment == null)
            throw new NullPointerException("alignment is null");
        format = format.with(alignment);
        return this;
    }
    
    public Cell center() {
        return align(Alignment.CENTER);
    }
    
    void setJoinedCell() {
        willSetContent();
        this.content = new JoinedContent();
    }
    
    private void willSetContent() {
        if (content != null)
            throw new IllegalStateException(String.format(
                "Cannot set content of %s, it is already set to %s", name(), content.toString()));
    }

    public Row row() {
        return row;
    }
    
    public Column column() {
        return column;
    }
    
    public Cell siblingInColumn(int c) {
        return row.cell(c);
    }

    public Cell siblingInColumn(Column c) {
        return siblingInColumn(c.ordinal());
    }
    
    public Cell siblingInRow(int r) {
        return row.sibling(r).cell(ordinal());
    }
    
    public Cell siblingInRow(Row r) {
        return siblingInRow(r.ordinal());
    }
    
    public Cell nextInRow() {
        return row.cell(ordinal() + 1);
    }
    
}
