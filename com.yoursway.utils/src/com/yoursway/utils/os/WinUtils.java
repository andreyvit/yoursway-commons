package com.yoursway.utils.os;

import java.io.File;
import java.io.IOException;

public class WinUtils implements OSUtils {
    
    public boolean isMacOSX() {
        return false;
    }
    
    public boolean isWindowsNT() {
        return true;
    }
    
    public String javaRelativePath() {
        return "bin\\java.exe";
    }
    
    public void setExecAttribute(File file) throws IOException {
        // windows doesn't use exec file attribute 
    }
    
    public void openBrowser(String url) throws IOException {
        String[] cmd = new String[4];
        cmd[0] = "cmd.exe";
        cmd[1] = "/C";
        cmd[2] = "start";
        cmd[3] = url;
        Runtime.getRuntime().exec(cmd);
    }
    
}
