package com.yoursway.utils.filespec;

import java.io.Serializable;

import com.yoursway.utils.fileset.FileSet;
import com.yoursway.utils.relativepath.RelativePath;

public class AllFilesSpec implements FileSetSpec, Serializable {

    private static final long serialVersionUID = 1L;

    public boolean contains(RelativePath file) {
		return true;
	}
	
	@Override
	public String toString() {
		return "*";
	}

	public boolean isKnownToBeEmpty() {
		return false;
	}
    
    public FileSet resolve(FileSet allFiles) {
        return allFiles;
    }

}
