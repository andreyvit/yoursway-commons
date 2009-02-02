package com.yoursway.commons.excelexport.internal;

import java.io.IOException;

public class RuntimeIOException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public RuntimeIOException(IOException exception) {
        super(exception);
        if (exception == null)
            throw new NullPointerException("exception is null");
    }
    
    public IOException getCause() {
        return (IOException) super.getCause();
    }
    
    
}
