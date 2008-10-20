package com.yoursway.utils.os;

import java.io.File;
import java.io.IOException;

public interface OSUtils {
    
    boolean isMacOSX();
    
    boolean isWindowsNT();
    
    void setExecAttribute(File file) throws IOException;
    
    String javaRelativePath();
    
    void openBrowser(String url) throws IOException;
    
}
