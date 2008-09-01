package com.yoursway.utils.io;

import java.io.Closeable;
import java.io.IOException;

public enum StreamCloseBehavior {
    
    IGNORE {

        public void close(Closeable delegate) throws IOException {
        }
        
    },
    
    PROPAGATE {

        public void close(Closeable delegate) throws IOException {
            delegate.close();
        }
        
    },
    
    UNSUPPORTED {

        public void close(Closeable delegate) throws IOException {
            throw new UnsupportedOperationException();
        }

    };
    
    public abstract void close(Closeable delegate) throws IOException;
    
}
