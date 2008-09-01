package com.yoursway.utils.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class RandomAccessFileInputStream extends InputStream {
    
    private final RandomAccessFile raf;
    private final StreamCloseBehavior closeBehavior;

    public RandomAccessFileInputStream(RandomAccessFile raf, StreamCloseBehavior closeBehavior) {
        if (raf == null)
            throw new NullPointerException("raf is null");
        if (closeBehavior == null)
            throw new NullPointerException("closeBehavior is null");
        this.raf = raf;
        this.closeBehavior = closeBehavior;
    }

    @Override
    public int read() throws IOException {
        return raf.read();
    }
    
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return raf.read(b, off, len);
    }
    
    @Override
    public void close() throws IOException {
        closeBehavior.close(raf);
    }

}
