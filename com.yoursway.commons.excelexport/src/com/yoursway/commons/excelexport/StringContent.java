package com.yoursway.commons.excelexport;

public class StringContent extends Content {
    
    private final String data;

    public StringContent(String data) {
        if (data == null)
            throw new NullPointerException("data is null");
        this.data = data;
    }

    @Override
    public void accept(ContentVisitor visitor) {
        visitor.visitStringContent(data);
    }
    
}
