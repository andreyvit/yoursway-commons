package com.yoursway.utils.filespec;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

import com.yoursway.utils.fileset.FileSet;
import com.yoursway.utils.relativepath.RelativePath;

public class ExcludedFileSpec implements FileSetSpec {
	
	private final FileSetSpec included;
	private final FileSetSpec excluded;

	public ExcludedFileSpec(FileSetSpec included, FileSetSpec excluded) {
		this.included = included;
		this.excluded = excluded;
	}

	public boolean contains(RelativePath file) {
		return included.contains(file) && !excluded.contains(file);
	}
	
	@Override
	public String toString() {
		if (excluded.isKnownToBeEmpty())
			return included.toString();
		else
			return "(" + included + " WITHOUT " + excluded + ")";
	}

	public boolean isKnownToBeEmpty() {
		return included.isKnownToBeEmpty();
	}

    public FileSet resolve(FileSet allFiles) {
        Set<RelativePath> result = newHashSet(included.resolve(allFiles).asCollection());
        result.removeAll(excluded.resolve(allFiles).asCollection());
        return new FileSet(result);
    }

}
