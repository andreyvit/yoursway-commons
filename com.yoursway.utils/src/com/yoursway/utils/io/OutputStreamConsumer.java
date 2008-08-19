package com.yoursway.utils.io;

import java.io.IOException;
import java.io.OutputStream;

public interface OutputStreamConsumer {
    
    void run(OutputStream out) throws IOException;
    
}
