package com.yoursway.utils;

import java.io.IOException;
import java.io.OutputStream;

public class DelegatingOutputStream extends OutputStream {

    protected final OutputStream target;

    public DelegatingOutputStream(OutputStream target) {
        if (target == null)
            throw new NullPointerException("target is null");
        this.target = target;
    }

    public void close() throws IOException {
        target.close();
    }

    public void flush() throws IOException {
        target.flush();
    }

    public void write(byte[] b, int off, int len) throws IOException {
        target.write(b, off, len);
    }

    public void write(byte[] b) throws IOException {
        target.write(b);
    }

    public void write(int b) throws IOException {
        target.write(b);
    }
    
    public OutputStream target() {
        return target;
    }
    
}
