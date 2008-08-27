package com.yoursway.utils.assertions;

import java.util.Arrays;
import java.util.List;

public class Assert {
    
    public static void assertion(boolean condition, String message) {
        if (condition)
            return;
        
        AssertionError error = new AssertionError(message);
        List<StackTraceElement> list = Arrays.asList(error.getStackTrace());
        List<StackTraceElement> subList = list.subList(1, list.size());
        StackTraceElement[] trace = subList.toArray(new StackTraceElement[list.size() - 1]);
        error.setStackTrace(trace);
        
        error.printStackTrace(System.err);
    }
    
}
