package com.yoursway.utils.log;

import java.io.PrintStream;

public class DefaultLogger implements Logger {
    
    public void add(LogEntry entry) {
        PrintStream stream = (entry.type() == LogEntryType.NOTIFICATION) ? System.out : System.err;
        stream.println(entry.message());
    }
    
}
