package com.yoursway.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

public class DelegatingZipClosingInputStream extends InputStream {

    private final InputStream in;
    private final ZipFile file;

    public DelegatingZipClosingInputStream(InputStream in, ZipFile file) {
        this.in = in;
        this.file = file;
    }

    public int available() throws IOException {
        return in.available();
    }

    public void close() throws IOException {
        file.close();
    }

    public void mark(int readlimit) {
        in.mark(readlimit);
    }

    public boolean markSupported() {
        return in.markSupported();
    }

    public int read() throws IOException {
        return in.read();
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    public int read(byte[] b) throws IOException {
        return in.read(b);
    }

    public void reset() throws IOException {
        in.reset();
    }

    public long skip(long n) throws IOException {
        return in.skip(n);
    }
    
    
}
