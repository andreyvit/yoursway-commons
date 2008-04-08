package com.yoursway.utils.fileset;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Collection;
import java.util.Set;

import com.yoursway.utils.relativepath.RelativePath;

public class FileSet {
    
    private final Set<RelativePath> files;

    public FileSet(Collection<RelativePath> files) {
        this.files = newHashSet(files);
    }
    
    public boolean contains(RelativePath relativePath) {
        return files.contains(relativePath);
    }

    public Collection<RelativePath> asCollection() {
        return files;
    }
    
}
