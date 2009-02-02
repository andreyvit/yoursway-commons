package com.yoursway.commons.excelexport.internal;

import java.io.InputStream;

public class Theme {
    
    public static InputStream getThemeFileAsStream() {
        return Theme.class.getResourceAsStream("theme1.xml");
    }
    
}
