package com.yoursway.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface StreamFilter {
    
    void process(InputStream in, OutputStream out) throws IOException;
    
}
