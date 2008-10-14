package com.yoursway.utils.os;

import static java.lang.Runtime.getRuntime;

import java.io.File;
import java.io.IOException;

public class MacUtils implements OSUtils {
    
    public boolean isMacOSX() {
        return true;
    }
    
    public boolean isWindowsNT() {
        return false;
    }
    
    public String javaRelativePath() {
        return "bin/java";
    }
    
    public void setExecAttribute(File file) throws IOException {
        String command = "/bin/chmod +x " + file.getCanonicalPath();
        Process process = getRuntime().exec(command);
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new Error("Error in setting exec file attr", e); //!
        }
    }
    
}
