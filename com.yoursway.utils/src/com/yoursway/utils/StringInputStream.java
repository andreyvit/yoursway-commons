package com.yoursway.utils;


import java.io.StringReader;

/**
 * Wraps a String as an InputStream.
 * 
 */
public class StringInputStream extends ReaderInputStream {
    
    /**
     * Composes a stream from a String
     * 
     * @param source
     *            The string to read from. Must not be <code>null</code>.
     */
    public StringInputStream(String source) {
        super(new StringReader(source));
    }
    
    /**
     * Composes a stream from a String with the specified encoding
     * 
     * @param source
     *            The string to read from. Must not be <code>null</code>.
     * @param encoding
     *            The encoding scheme. Also must not be <code>null</code>.
     */
    public StringInputStream(String source, String encoding) {
        super(new StringReader(source), encoding);
    }
    
}
