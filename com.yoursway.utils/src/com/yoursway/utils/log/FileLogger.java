package com.yoursway.utils.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

import com.yoursway.utils.annotations.Nullable;

public class FileLogger implements Logger {
    
    private final File file;
    
    public FileLogger(@Nullable String filenamePrefix) throws IOException {
        String prefix = filenamePrefix == null ? "yoursway-commons" : filenamePrefix;
        file = File.createTempFile(prefix + "-", ".log");
        
        FileOutputStream stream = new FileOutputStream(file, true);
        PrintWriter writer = new PrintWriter(stream);
        writer.println("----");
        writer.println(currentTime());
        writer.close();
    }
    
    private Date currentTime() {
        return Calendar.getInstance().getTime();
    }
    
    public void add(LogEntry entry) {
        FileOutputStream stream;
        try {
            stream = new FileOutputStream(file, true);
            PrintWriter writer = new PrintWriter(stream);
            
            writer.printf("%s :%s: %s\n", currentTime(), entry.type(), entry.message());
            
            writer.close();
        } catch (IOException e) {
            System.out.printf("%s: %s", entry.type(), entry.message());
            System.err.println("Cannot write to log file.");
            e.printStackTrace();
        }
    }
    
}
