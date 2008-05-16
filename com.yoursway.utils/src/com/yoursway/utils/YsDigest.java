package com.yoursway.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class YsDigest {
    
    public static String sha1(String data) {
        try {
            MessageDigest algo = createSha1();
            byte[] bytes = data.getBytes("utf-8");
            algo.update(bytes);
            return asHex(algo.digest());
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
        
    }
    
    public static String md5(String data) {
        try {
            MessageDigest algo = createMd5();
            byte[] bytes = data.getBytes("utf-8");
            algo.update(bytes);
            return asHex(algo.digest());
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
        
    }
    
    public static String md5(InputStream stream) throws IOException {
        try {
            MessageDigest algo = createMd5();
            byte[] buf = new byte[1 * 1024 * 1024];
            int len = stream.read(buf);
            while (len > 0) {
                algo.update(buf, 0, len);
                len = stream.read(buf);
            }
            return asHex(algo.digest());
        } finally {
            stream.close();
        }
    }
    
    private static MessageDigest createMd5() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }
    
    private static MessageDigest createSha1() {
        try {
            return MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }
    
    private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f', };
    
    /**
     * Turns array of bytes into string representing each byte as unsigned hex
     * number.
     * 
     * Copied from fast_md5 implementation, see
     * http://www.twmacinta.com/myjava/fast_md5.php
     * 
     * @param hash
     *            Array of bytes to convert to hex-string
     * @return Generated hex string
     */
    private static String asHex(byte hash[]) {
        char buf[] = new char[hash.length * 2];
        for (int i = 0, x = 0; i < hash.length; i++) {
            buf[x++] = HEX_CHARS[(hash[i] >>> 4) & 0xf];
            buf[x++] = HEX_CHARS[hash[i] & 0xf];
        }
        return new String(buf);
    }
    
}
