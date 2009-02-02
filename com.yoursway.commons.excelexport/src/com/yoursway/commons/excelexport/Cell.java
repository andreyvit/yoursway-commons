package com.yoursway.commons.excelexport;

public class Cell {
    
    private final int ordinal;
    
    private final Row row;
    
    private Content content;
    
    private int horizonalSpan = 1;
    
    public Cell(Row row, int ordinal) {
        this.row = row;
        this.ordinal = ordinal;
    }
    
    public int ordinal() {
        return ordinal;
    }
    
    public int horizonalSpan() {
        return horizonalSpan;
    }
    
    public void setHorizonalSpan(int horizonalSpan) {
        this.horizonalSpan = horizonalSpan;
        for (int ordinal = this.ordinal + 1; ordinal < this.ordinal + horizonalSpan; ordinal++)
            row.cell(ordinal).setJoinedCell();
    }
    
    public Cell finalMergedCell() {
        return row.cell(ordinal + horizonalSpan - 1);
    }
    
    public String colName() {
        int index = ordinal - 1;
        if (ordinal < 26)
            return letterWithIndex(index);
        else
            return letterWithIndex(index / 26) + letterWithIndex(index % 26);
    }

    private static String letterWithIndex(int index) {
        return Character.toString((char) ('A' + index));
    }
    
    public String name() {
        return colName() + row.ordinal();
    }
    
    public Cell setText(String text) {
        willSetContent();
        this.content = new StringContent(text);
        return this;
    }
    
    void setJoinedCell() {
        willSetContent();
        this.content = new JoinedContent();
    }
    
    public void acceptContentVisitor(ContentVisitor visitor) {
        if (content == null)
            visitor.visitNullContent();
        else
            content.accept(visitor);
    }

    private void willSetContent() {
        if (content != null)
            throw new IllegalStateException(String.format("Cannot set content of %s, it is already set to %s", 
                name(), content.toString()));
    }
    
}
