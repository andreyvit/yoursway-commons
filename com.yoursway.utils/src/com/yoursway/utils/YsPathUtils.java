package com.yoursway.utils;

import static com.yoursway.utils.YsPathUtils.isPathSeparator;

import java.io.File;

public class YsPathUtils {
    
    public static String extension(File file) {
        return extensionOfName(file.getName());
    }
    
    public static String extensionOfName(String fileName) {
        int pos = fileName.lastIndexOf('.');
        if (pos >= 0)
            return fileName.substring(pos + 1);
        else
            return null;
    }
    
    public static String joinPath(String a, String b) {
        if (a.length() == 0)
            return b;
        if (b.length() == 0)
            return a;
        boolean suffixed = a.endsWith("/");
        boolean prefixed = b.startsWith("/");
        if (suffixed && prefixed)
            return a + b.substring(1);
        else if (!suffixed && !prefixed)
            return a + "/" + b;
        else
            return a + b;
    }
    
    public static String removeTrailingSeparator(String directoryPath) {
        if (directoryPath.endsWith("/") || directoryPath.endsWith(File.pathSeparator))
            return directoryPath.substring(0, directoryPath.length() - 1);
        else
            return directoryPath;
    }
    
    public static boolean isPathSeparator(char ch) {
        return ch == '/' || ch == File.pathSeparatorChar;
    }
    
    public static boolean isChildOrParent(String first, String second) {
        int firstLength = first.length();
        int secondLength = second.length();
        if (secondLength > firstLength)
            // might be a child
            return second.regionMatches(0, first, 0, firstLength)
                    && isPathSeparator(second.charAt(firstLength));
        else if (secondLength < firstLength)
            // might be a parent
            return first.regionMatches(0, second, 0, secondLength)
                    && isPathSeparator(first.charAt(secondLength));
        else
            // pathLength == monitoredPathLength
            return second.equals(first);
    }
    
}
