package com.yoursway.utils.log;

import java.io.IOException;
import java.io.OutputStream;

public class LogOutputStream extends OutputStream {
    
    private final StringBuilder message = new StringBuilder();
    private final LogEntryType type;
    private final StackTraceElement place;
    
    LogOutputStream(LogEntryType type, StackTraceElement place) {
        this.type = type;
        this.place = place;
    }
    
    @Override
    public void write(int b) throws IOException {
        message.append((char) b);
    }
    
    @Override
    public void close() throws IOException {
        super.close();
        Log.write(message.toString(), type, place);
    }
    
}
