package com.yoursway.commons.excelexport;

public abstract class Content {
    
    public abstract void accept(ContentVisitor visitor);
    
}
