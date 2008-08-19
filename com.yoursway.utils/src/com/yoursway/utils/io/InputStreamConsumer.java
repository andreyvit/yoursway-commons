package com.yoursway.utils.io;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamConsumer {
    
    void run(InputStream in) throws IOException;
    
}
