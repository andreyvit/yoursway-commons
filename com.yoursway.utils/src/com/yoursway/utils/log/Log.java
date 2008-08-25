package com.yoursway.utils.log;

public class Log {
    
    private static Logger logger = new DefaultLogger();
    
    public static void setLogger(Logger logger) {
        Log.logger = logger;
    }
    
    private static void write(String message, LogEntryType type, StackTraceElement place) {
        logger.add(new LogEntry(message, type, place));
    }
    
    public static void write(String message, LogEntryType type) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        write(message, type, stackTrace[3]);
    }
    
    public static void write(String message) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        write(message, LogEntryType.NOTIFICATION, stackTrace[3]);
    }
    
}
