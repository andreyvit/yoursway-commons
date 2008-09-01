package com.yoursway.utils.storage.atomic;

import static java.lang.String.format;

import java.io.File;

public class ConcurrentFileModificationException extends Exception {
    
    private static final String MESSAGE = "File %s cannot be written to because it has been modified since it was read; old hash is %s, new hash is %s.";
    
    private static final long serialVersionUID = 1L;
    
    public ConcurrentFileModificationException(File file, String hash, String hash2) {
        super(format(MESSAGE, file, hash, hash2));
    }
    
}
