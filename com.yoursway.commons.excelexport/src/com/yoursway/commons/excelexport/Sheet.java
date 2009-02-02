package com.yoursway.commons.excelexport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sheet {
    
    private final String name;
    private final int ordinal;
    
    private final List<Row> rows = new ArrayList<Row>();
    
    public Sheet(int ordinal, String name) {
        if (name == null)
            throw new NullPointerException("name is null");
        this.ordinal = ordinal;
        this.name = name;
        row(1); // make sure at least one row exists
    }
    
    public int ordinal() {
        return ordinal;
    }
    
    public String name() {
        return name;
    }
    
    public Row row(int ordinal) {
        while (rows.size() < ordinal)
            rows.add(new Row(rows.size() + 1));
        return rows.get(ordinal - 1);
    }
    
    public int columnCount() {
        int columns = 0;
        for (Row row : rows)
            columns = Math.max(columns, row.cells().size());
        return columns;
    }
    
    public Cell bottomRightCell() {
        return row(rows.size()).cell(columnCount());
    }
    
    public Cell cell(int row, int cell) {
        return row(row).cell(cell);
    }
    
    public List<Row> rows() {
        return Collections.unmodifiableList(rows);
    }
    
}
