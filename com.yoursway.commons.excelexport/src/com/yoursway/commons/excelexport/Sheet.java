package com.yoursway.commons.excelexport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sheet {
    
    private final String name;
    private final int ordinal;
    
    private final List<Row> rows = new ArrayList<Row>();
    private final List<Column> columns = new ArrayList<Column>();
    
    public Sheet(Workbook workbook, int ordinal, String name) {
        if (workbook == null)
            throw new NullPointerException("workbook is null");
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
            rows.add(new Row(this, rows.size() + 1));
        return rows.get(ordinal - 1);
    }
    
    public Column column(int ordinal) {
        while (columns.size() < ordinal)
            columns.add(new Column(this, columns.size() + 1));
        return columns.get(ordinal - 1);
    }
    
    public List<Column> columns() {
        return Collections.unmodifiableList(columns);
    }
    
    public int columnCount() {
        int count = columns.size();;
        for (Row row : rows)
            count = Math.max(count, row.cells().size());
        return count;
    }
    
    public Range range() {
        return new Range(cell(1, 1), cell(rows.size(), columns.size()));
    }
    
    public Cell cell(int row, int cell) {
        return row(row).cell(cell);
    }
    
    public List<Row> rows() {
        return Collections.unmodifiableList(rows);
    }
 
}
