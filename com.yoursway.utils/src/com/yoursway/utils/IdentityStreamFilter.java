package com.yoursway.utils;

import static com.yoursway.utils.YsFileUtils.transfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IdentityStreamFilter implements StreamFilter {

    public void process(InputStream in, OutputStream out) throws IOException {
        transfer(in, out);
    }
    
}
