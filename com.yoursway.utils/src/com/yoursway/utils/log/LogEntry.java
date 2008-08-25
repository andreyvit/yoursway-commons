package com.yoursway.utils.log;

public class LogEntry {
    
    private final LogEntryType type;
    private final StackTraceElement place;
    private final String message;
    
    public LogEntry(String message, LogEntryType type, StackTraceElement place) {
        this.message = message;
        this.type = type;
        this.place = place;
    }
    
    public LogEntryType type() {
        return type;
    }
    
    public StackTraceElement place() {
        return place;
    }
    
    public String message() {
        return message;
    }
    
}
