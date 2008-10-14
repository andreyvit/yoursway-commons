package com.yoursway.utils.os;

import java.io.File;
import java.io.IOException;

public class YsOSUtils {
    
    private static OSUtils os;
    
    private static OSUtils os() {
        if (os == null) {
            String osName = System.getProperty("os.name");
            
            if (osName.equals("Mac OS X"))
                os = new MacUtils();
            else if (osName.equals("Windows NT"))
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
    
    public static String javaRelativePath() {
        return os().javaRelativePath();
    }
    
}
