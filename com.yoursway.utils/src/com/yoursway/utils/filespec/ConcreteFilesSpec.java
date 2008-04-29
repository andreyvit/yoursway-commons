package com.yoursway.utils.filespec;

import static com.google.common.collect.Lists.newArrayListWithCapacity;
import static com.yoursway.utils.YsStrings.sortedToString;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;
import com.yoursway.utils.fileset.FileSet;
import com.yoursway.utils.relativepath.RelativePath;

public class ConcreteFilesSpec implements FileSetSpec, Serializable {
	
    private static final long serialVersionUID = 1L;
    
    private final Set<RelativePath> files;

	public ConcreteFilesSpec(Collection<RelativePath> files) {
		this.files = Sets.newHashSet(files);
	}

	public boolean contains(RelativePath file) {
		return files.contains(file);
	}
	
	@Override
	public String toString() {
		return sortedToString(files);
	}

	public boolean isKnownToBeEmpty() {
		return files.isEmpty();
	}
	   
    public FileSet resolve(FileSet allFiles) {
        Collection<RelativePath> validPathes = newArrayListWithCapacity(files.size());
        for (RelativePath file : files)
            if (allFiles.contains(file))
                validPathes.add(file);
        return new FileSet(validPathes);
    }

}
