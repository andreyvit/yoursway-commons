package com.yoursway.commons.preferences.core;

import static java.lang.String.format;

public class Snippet {
    
    private ThreadLocal<String> v = new ThreadLocal<String>();
    
    private String f;
    
    private volatile String vf;
    
    public static void main(String[] args) {
        new Snippet().run();
    }
    
    private void run() {
        int loops = 50000000;
        int repeat = 5;
        System.out.println(format("Performing %d loops for each method...", loops));
        for (int i = 0; i < repeat; i++) {
            System.out.println();
            output("Local", measureLocal(loops), loops);
            output("Field", measureField(loops), loops);
            output("Volatile field", measureVolatileField(loops), loops);
            output("Sync. field", measureSynchronizedField(loops), loops);
            output("ThreadLocal", measureThreadLocal(loops), loops);
        }
    }

    private void output(String mode, long span, int loops) {
        System.out.println(format("%-15s: %5d ms total, %.3f ms per 1M accesses", mode, span, span * 1000000.0 / loops));
    }

    private long measureThreadLocal(int loops) {
        long start = System.currentTimeMillis();
        v.set("Foo");
        int countEmpty = 0;
        for (int i = 0; i < loops; i++) {
            if (v.get().length() == 0)
                countEmpty++;
        }
        long end = System.currentTimeMillis();
        return end - start;
    }
    
    private long measureField(int loops) {
        long start = System.currentTimeMillis();
        f = "Foo";
        int countEmpty = 0;
        for (int i = 0; i < loops; i++) {
            if (f.length() == 0)
                countEmpty++;
        }
        long end = System.currentTimeMillis();
        return end - start;
    }
    
    private long measureSynchronizedField(int loops) {
        Object sync = new Object();
        long start = System.currentTimeMillis();
        f = "Foo";
        int countEmpty = 0;
        for (int i = 0; i < loops; i++) {
            synchronized(sync) {
                if (f.length() == 0)
                    countEmpty++;
            }
        }
        long end = System.currentTimeMillis();
        return end - start;
    }
    
    private long measureLocal(int loops) {
        long start = System.currentTimeMillis();
        String lcl = "Foo";
        int countEmpty = 0;
        for (int i = 0; i < loops; i++) {
            if (lcl.length() == 0)
                countEmpty++;
        }
        long end = System.currentTimeMillis();
        return end - start;
    }
    
    private long measureVolatileField(int loops) {
        long start = System.currentTimeMillis();
        vf = "Foo";
        int countEmpty = 0;
        for (int i = 0; i < loops; i++) {
            if (vf.length() == 0)
                countEmpty++;
        }
        long end = System.currentTimeMillis();
        return end - start;
    }
    
}
