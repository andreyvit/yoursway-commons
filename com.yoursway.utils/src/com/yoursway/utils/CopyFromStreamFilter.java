package com.yoursway.utils;

import static com.yoursway.utils.YsFileUtils.transfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyFromStreamFilter implements StreamFilter {
    
    private final InputStream source;

    public CopyFromStreamFilter(InputStream source) {
        if (source == null)
            throw new NullPointerException("source");
        this.source = source;
    }

    public void process(InputStream in, OutputStream out) throws IOException {
        if (out == null)
            throw new NullPointerException("out");
        transfer(source, out);
    }
    
}
