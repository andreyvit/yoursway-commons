package com.yoursway.utils.io;

import static java.lang.Math.min;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import com.yoursway.utils.YsDigest;

public class HashingInputStreamDecorator extends FilterInputStream {

    private final MessageDigest algorithm;
    
    private boolean eofReached = false;

    private final StreamCloseBehavior closeBehavior;
    
    public static HashingInputStreamDecorator md5HashingDecorator(InputStream in, StreamCloseBehavior closeBehavior) {
        return new HashingInputStreamDecorator(in, closeBehavior, YsDigest.createMd5());
    }
    
    public static HashingInputStreamDecorator sha1HashingDecorator(InputStream in, StreamCloseBehavior closeBehavior) {
        return new HashingInputStreamDecorator(in, closeBehavior, YsDigest.createSha1());
    }

    public HashingInputStreamDecorator(InputStream in, StreamCloseBehavior closeBehavior, MessageDigest algorithm) {
        super(in);
        if (algorithm == null)
            throw new NullPointerException("algorithm is null");
        if (closeBehavior == null)
            throw new NullPointerException("closeBehavior is null");
        this.algorithm = algorithm;
        this.closeBehavior = closeBehavior;
    }

    @Override
    public synchronized void mark(int readlimit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public synchronized void reset() throws IOException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int read() throws IOException {
        int result = super.read();
        if (result >= 0)
            algorithm.update((byte) result);
        else
            eofReached = true;
        return result;
    }
    
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int result = super.read(b, off, len);
        if (result > 0) 
            algorithm.update(b, off, result);
        else
            eofReached = true;
        return result;
    }

    @Override
    public long skip(long n) throws IOException {
        byte[] dummy = new byte[102400];
        long result = 0;
        while (n > 0) {
            int portion = (n > Integer.MAX_VALUE ? dummy.length : min((int) n, dummy.length));
            int len = read(dummy, 0, portion); 
            if (len <= 0)
                break;
            n -= len;
            result += len;
        }
        return result;
    }
    
    @Override
    public void close() throws IOException {
        closeBehavior.close(in);
    }
   
    public boolean isEofReached() {
        return eofReached;
    }
    
    public byte[] getHash() {
        if (!eofReached)
            throw new IllegalStateException("Cannot obtain the hash until EOF has been reached.");
        return algorithm.digest();
    }
    
    public String getHexHash() {
        return YsDigest.asHex(getHash());
    }
    
}
