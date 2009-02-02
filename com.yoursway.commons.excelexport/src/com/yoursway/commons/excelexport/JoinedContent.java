package com.yoursway.commons.excelexport;

public class JoinedContent extends Content {

    @Override
    public void accept(ContentVisitor visitor) {
        visitor.visitNullContent();
    }
    
}
