package com.yoursway.utils.relativepath;

public class Pathes {

	public static RelativePath relativePath(String path) {
		return new RelativePathImpl(path);
	}

}
