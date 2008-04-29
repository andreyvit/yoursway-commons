package com.yoursway.utils.filespec;

import static com.google.common.base.Functions.TO_STRING;
import static com.google.common.base.Join.join;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Sets.newHashSet;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.yoursway.utils.fileset.FileSet;
import com.yoursway.utils.relativepath.RelativePath;

public class CompoundFileSetSpec implements FileSetSpec, Serializable {
	
    private static final long serialVersionUID = 1L;
    
    private List<FileSetSpec> children = Lists.newArrayList();
	
	public void add(FileSetSpec spec) {
		children.add(spec);
	}

	public boolean contains(RelativePath file) {
		for (FileSetSpec spec : children)
			if (spec.contains(file))
				return true;
		return false;
	}
	
	@Override
	public String toString() {
		if (children.size() == 1)
			return children.iterator().next().toString();
		else
			return "(" + join(" UNION ", transform(children, TO_STRING)) + ")";
	}

	public boolean isKnownToBeEmpty() {
		return children.isEmpty();
	}

    public FileSet resolve(FileSet allFiles) {
        Set<RelativePath> result = newHashSet();
        for (FileSetSpec child : children)
            result.addAll(child.resolve(allFiles).asCollection());
        return new FileSet(result);
    }

}
