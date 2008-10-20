package com.yoursway.utils.log;

import java.io.OutputStream;

public class Log {
    
    private static Logger logger = new DefaultLogger();
    
    public static void setLogger(Logger logger) {
        Log.logger = logger;
    }
    
    static void write(String message, LogEntryType type, StackTraceElement place) {
        logger.add(new LogEntry(message, type, place));
    }
    
    @Deprecated
    public static void write(String message, LogEntryType type) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        write(message, type, stackTrace[3]);
    }
    
    public static void writeError(String message) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        write(message, LogEntryType.ERROR, stackTrace[3]);
    }

    public static void write(String message) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        write(message, LogEntryType.NOTIFICATION, stackTrace[3]);
    }
    
    public static OutputStream stream() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return new LogOutputStream(LogEntryType.NOTIFICATION, stackTrace[3]);
    }
    
}
