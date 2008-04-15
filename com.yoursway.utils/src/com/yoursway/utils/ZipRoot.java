package com.yoursway.utils;

import static com.google.common.collect.Lists.newArrayList;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

public class ZipRoot {
    
    private static final Collection<File> NO_EXCLUDES = Collections.emptyList();
    
    private final File folder;
    private final String prefix;

    private final Collection<File> excludes;

    public ZipRoot(File folder, String prefix) {
        this(folder, prefix, NO_EXCLUDES);
    }
    
    public ZipRoot(File folder, String prefix, Collection<File> excludes) {
        if (folder == null)
            throw new NullPointerException();
        if (prefix == null)
            throw new NullPointerException();
        if (excludes == null)
            throw new NullPointerException();
        this.folder = folder;
        this.prefix = prefix;
        this.excludes = newArrayList(excludes);
    }
    
    public File folder() {
        return folder;
    }

    public String prefix() {
        return prefix;
    }
    
    public Collection<File> excludes() {
        return excludes;
    }
    
}
