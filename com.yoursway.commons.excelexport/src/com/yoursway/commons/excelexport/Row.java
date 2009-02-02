package com.yoursway.commons.excelexport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Row {
    
    private final List<Cell> cells = new ArrayList<Cell>();
    private final int ordinal;
    
    Row(int ordinal) {
        this.ordinal = ordinal;
        cell(1); // make sure at least one cell exists
    }
    
    public int ordinal() {
        return ordinal;
    }
    
    public Cell cell(int ordinal) {
        while (cells.size() < ordinal)
            cells.add(new Cell(this, cells.size() + 1));
        return cells.get(ordinal - 1);
    }
    
    public Cell lastCell() {
        return cell(cells.size());
    }
    
    public List<Cell> cells() {
        return Collections.unmodifiableList(cells);
    }
    
}
