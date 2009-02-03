package com.yoursway.commons.excelexport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Row {
    
    private final List<Cell> cells = new ArrayList<Cell>();
    private final int ordinal;
    private final Sheet sheet;
    
    Row(Sheet sheet, int ordinal) {
        if (sheet == null)
            throw new NullPointerException("sheet is null");
        this.sheet = sheet;
        this.ordinal = ordinal;
        cell(1); // make sure at least one cell exists
    }
    
    public int ordinal() {
        return ordinal;
    }
    
    public Cell cell(int ordinal) {
        while (cells.size() < ordinal)
            cells.add(new Cell(this, sheet.column(cells.size() + 1)));
        return cells.get(ordinal - 1);
    }
    
    public Cell lastCell() {
        return cell(cells.size());
    }
    
    public List<Cell> cells() {
        return Collections.unmodifiableList(cells);
    }
    
    public Sheet sheet() {
        return sheet;
    }

    public Row next() {
        return sheet.row(ordinal + 1);
    }

    public Cell cell(Column column) {
        return cell(column.ordinal());
    }

    public Row sibling(int ordinal) {
        return sheet.row(ordinal);
    }
    
}
