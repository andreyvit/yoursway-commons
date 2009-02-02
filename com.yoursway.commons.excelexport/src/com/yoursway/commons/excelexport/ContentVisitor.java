package com.yoursway.commons.excelexport;

public interface ContentVisitor {
    
    void visitStringContent(String data);
    
    void visitNullContent();
    
}
