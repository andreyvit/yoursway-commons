package com.yoursway.commons.excelexport;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Range {
    
    private final Cell start;
    private final Cell end;
    
    public Range(Cell start, Cell end) {
        if (start == null)
            throw new NullPointerException("start is null");
        if (end == null)
            throw new NullPointerException("end is null");
        this.start = start;
        this.end = end;
    }
    
    //    public Cell start() {
    //        return start;
    //    }
    //    
    //    public Cell end() {
    //        return end;
    //    }
    
    public String name() {
        return start.name() + ":" + end.name();
    }
    
    public Range reduceToColumn(int column) {
        return new Range(start.siblingInColumn(column), end.siblingInColumn(column));
    }
    
    public Range reduceToColumn(Column column) {
        return new Range(start.siblingInColumn(column), end.siblingInColumn(column));
    }
    
    public Range reduceToRow(int row) {
        return new Range(start.siblingInRow(row), end.siblingInRow(row));
    }
    
    public Range reduceToRow(Row row) {
        return new Range(start.siblingInRow(row), end.siblingInRow(row));
    }
    
    public Range startingAtColumn(int column) {
        return startingAtColumn(start.siblingInColumn(column).column());
    }
    
    public Range startingAtColumn(Column column) {
        return new Range(start.siblingInColumn(column), end);
    }
    
    public Range startingAtRow(int row) {
        return new Range(start.siblingInRow(row), end);
    }
    
    public Range startingAtRow(Row row) {
        return new Range(start.siblingInRow(row), end);
    }
    
    public List<Cell> cells() {
        List<Cell> result = new ArrayList<Cell>();
        for (Row row = start.row(), lastRow = end.row();; row = row.next()) {
            for (Column column = start.column(), lastColumn = end.column();; column = column.next()) {
                result.add(row.cell(column));
                if (column == lastColumn)
                    break;
            }
            if (row == lastRow)
                break;
        }
        return result;
    }
    
    public List<Column> columns() {
        List<Column> result = new ArrayList<Column>();
        for (Column current = start.column(), lastColumn = end.column();; current = current.next()) {
            result.add(current);
            if (current == lastColumn)
                break;
        }
        return result;
    }
    
    public int columnCount() {
        return end.column().ordinal() - start.column().ordinal() + 1;
    }
    
    public Range columnWidth(int widthInChars) {
        for (Column column : columns())
            column.width(widthInChars);
        return this;
    }

    public Range fill(Fill fill) {
        for (Cell cell : cells())
            cell._fill(fill);
        return this;
    }
    
    public Cell merge() {
        start.hspan(columnCount());
        return start;
    }
    
    public Range align(Alignment alignment) {
        for (Cell cell : cells())
            cell.align(alignment);
        return this;
    }
    
    public Range center() {
        return align(Alignment.CENTER);
    }
    
    public Range outerBorder(Border border) {
        return outerBorder(border, Edge.OUTER);
    }
    
    public Range outerBorder(Border border, Edge edge) {
        return outerBorder(border, EnumSet.of(edge));
    }
    
    public Range outerBorder(Border border, EnumSet<Edge> edges) {
        if (edges.contains(Edge.LEFT))
            for (Cell cell : reduceToColumn(start.column()).cells())
                cell.border(border, Edge.LEFT);
        if (edges.contains(Edge.TOP))
            for (Cell cell : reduceToRow(start.row()).cells())
                cell.border(border, Edge.TOP);
        if (edges.contains(Edge.RIGHT))
            for (Cell cell : reduceToColumn(end.column()).cells())
                cell.border(border, Edge.RIGHT);
        if (edges.contains(Edge.BOTTOM))
            for (Cell cell : reduceToRow(end.row()).cells())
                cell.border(border, Edge.BOTTOM);
        return this;
    }
    
    public Range innerBorder(Border border) {
        return innerBorder(border, true, true);
    }
    
    public Range innerBorder(Border border, boolean horizonal, boolean vertical) {
        if (horizonal)
            for (Cell cell : cells()) {
                if (cell.row() != start.row())
                    cell.border(border, Edge.TOP);
                if (cell.row() != end.row())
                    cell.border(border, Edge.BOTTOM);
            }
        if (vertical)
            for (Cell cell : cells()) {
                if (cell.column() != start.column())
                    cell.border(border, Edge.LEFT);
                if (cell.column() != end.column())
                    cell.border(border, Edge.RIGHT);
            }
        return this;
    }
    
}
