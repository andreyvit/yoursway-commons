package com.yoursway.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class URLs {
    
    public static URL appendPath(URL url, String path) throws MalformedURLException {
        return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getPath() + path);
    }
    
}
