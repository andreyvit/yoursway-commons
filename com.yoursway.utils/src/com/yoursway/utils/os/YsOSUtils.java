package com.yoursway.utils.os;

import java.io.File;
import java.io.IOException;

public class YsOSUtils {
    
    private static OSUtils os;
    
    private static OSUtils os() {
        if (os == null) {
            String osName = System.getProperty("os.name");
            String lowercased = osName.toLowerCase();
            
            if (lowercased.contains("mac"))
                os = new MacUtils();
            else if (lowercased.contains("win"))
                os = new WinUtils();
            
            if (os == null)
                throw new Error("Unknown OS " + osName);
        }
        
        return os;
    }
    
    public static boolean isMacOSX() {
        return os().isMacOSX();
    }
    
    public static boolean isWindowsNT() {
        return os().isWindowsNT();
    }
    
    public static void setExecAttribute(File file) throws IOException {
        os().setExecAttribute(file);
    }
    
    private static String javaRelativePath() {
        return os().javaRelativePath();
    }
    
    public static String javaPath() throws IOException {
        String javaHome = System.getProperty("java.home");
        File javaFile = new File(javaHome, javaRelativePath());
        return javaFile.getCanonicalPath();
    }
    
    public static void openBrowser(String url) throws IOException {
        os().openBrowser(url);
    }
}
