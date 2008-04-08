package com.yoursway.utils.filespec;

import com.yoursway.utils.fileset.FileSet;
import com.yoursway.utils.relativepath.RelativePath;

public interface FileSetSpec {
	
    FileSet resolve(FileSet allFiles);

	boolean contains(RelativePath file);
	
	boolean isKnownToBeEmpty();

}
