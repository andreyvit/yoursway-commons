package com.yoursway.utils.filespec;

import com.yoursway.utils.fileset.FileSet;
import com.yoursway.utils.relativepath.RelativePath;

public class AllFilesSpec implements FileSetSpec {

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
