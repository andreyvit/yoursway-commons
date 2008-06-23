package com.yoursway.utils;

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
    
}
