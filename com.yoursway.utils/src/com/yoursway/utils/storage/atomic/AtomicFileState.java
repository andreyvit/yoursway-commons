package com.yoursway.utils.storage.atomic;

import java.io.IOException;

import com.yoursway.utils.io.OutputStreamConsumer;

public interface AtomicFileState {
    
    void write(OutputStreamConsumer consumer) throws IOException, ConcurrentFileModificationException;
    
}
