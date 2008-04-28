package com.yoursway.utils;

import java.io.IOException;
import java.io.InputStream;

public class DelegatingInputStream extends InputStream {
    
    protected final InputStream target;

    public DelegatingInputStream(InputStream target) {
        if (target == null)
            throw new NullPointerException("target is null");
        this.target = target;
    }

    public int available() throws IOException {
        return target.available();
    }

    public void close() throws IOException {
        target.close();
    }

    public void mark(int readlimit) {
        target.mark(readlimit);
    }

    public boolean markSupported() {
        return target.markSupported();
    }

    public int read() throws IOException {
        return target.read();
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return target.read(b, off, len);
    }

    public int read(byte[] b) throws IOException {
        return target.read(b);
    }

    public void reset() throws IOException {
        target.reset();
    }

    public long skip(long n) throws IOException {
        return target.skip(n);
    }
    
}
