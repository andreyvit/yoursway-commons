package com.yoursway.utils.tests;

import junit.framework.Assert;

import org.junit.Test;

import com.yoursway.utils.TagTimer;

public class TagTimerExample {
    
    public static void main(String[] args) {
        new TagTimerExample().bigTest();
    }
    
    private StringBuilder log = new StringBuilder();
    
    void println(String message) {
        System.out.println(message);
        log.append(message).append("\n");
    }
    
    @Test
    public void bigTest() {
        final TagTimer timer = new TagTimer("Timer Thread", true);
        final Runnable q = new Runnable() {
            public void run() {
                println("q");
            }
        };
        final Runnable n = new Runnable() {
            public void run() {
                println("n");
            }
        };
        final Runnable p = new Runnable() {
            public void run() {
                println("periodical");
                timer.reschedule(1200, 1800, n, "N");
            }
        };
        timer.reschedulePeriodical(300, 200, p, "P");
        timer.schedule(100, new Runnable() {
            public void run() {
                println("hello");
                timer.reschedulePeriodical(500, 500, p, "P");
                timer.reschedule(1100, 1800, q, "Q");
            }
        });
        timer.reschedule(700, 1800, q, "Q");
        timer.reschedule(1200, 1800, n, "N");
        try {
            Thread.sleep(2300);
        } catch (InterruptedException e) {
        }
        Assert.assertEquals("hello\n" + "periodical\n" + "periodical\n" + "q\n" + "periodical\n" + "n\n"
                + "periodical\n", log.toString());
    }
    
}

class DataType1 extends Object {
    
}

class DataType2 extends Object {
    
}

class MyTask implements Runnable {
    
    private static final String TAG = MyTask.class.getName();
    
    private final DataType1 value1;
    private final DataType2 value2;
    
    public MyTask(DataType1 value1, DataType2 value2) {
        this.value1 = value1;
        this.value2 = value2;
    }
    
    public void scheduleIn(TagTimer timer, long delay) {
        timer.reschedule(delay, 60000, this, value1, value2, TAG);
    }
    
    public static void cancelAllTasks(TagTimer timer, DataType1 value1) {
        timer.cancel(value1, TAG);
    }
    
    public static void cancelAllTasks(TagTimer timer, DataType2 value2) {
        timer.cancel(value2, TAG);
    }
    
    public void run() {
        // ...
    }
    
}
