package com.yoursway.utils.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class RandomAccessFileOutputStream extends OutputStream {
    
    private final RandomAccessFile raf;
    private final StreamCloseBehavior closeBehavior;

    public RandomAccessFileOutputStream(RandomAccessFile raf, StreamCloseBehavior closeBehavior) {
        if (raf == null)
            throw new NullPointerException("raf is null");
        if (closeBehavior == null)
            throw new NullPointerException("closeBehavior is null");
        this.raf = raf;
        this.closeBehavior = closeBehavior;
    }

    @Override
    public void write(int b) throws IOException {
        raf.write(b);
    }
    
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        raf.write(b, off, len);
    }
    
    @Override
    public void close() throws IOException {
        closeBehavior.close(raf);
    }
    
}
