package com.yoursway.progress.core.tests;

import org.junit.Before;
import org.junit.Test;

import com.yoursway.progress.core.ItemizedProgress;
import com.yoursway.progress.core.Naming;
import com.yoursway.progress.core.Progress;
import com.yoursway.progress.core.ProgressTracking;
import com.yoursway.progress.core.tests.mocks.ActionReporter;

public class ActionTests {
    
    protected ActionReporter reporter;
    protected Progress progress;
    
    @Before
    public void createReporter() {
        reporter = new ActionReporter();
        progress = ProgressTracking.track(reporter);
    }
    
    @Test
    public void setTaskName() {
        progress.setTaskName("foo");
        reporter.expect("foo");
    }
    
    @Test
    public void childName() {
        progress.setTaskName("foo");
        Progress p1 = progress.subtask(1, Naming.AS_CHILDREN);
        p1.setTaskName("bar");
        reporter.expect("foo/bar");
    }
    
    @Test
    public void nameReset() {
        progress.setTaskName("foo");
        Progress p1 = progress.subtask(1, Naming.AS_CHILDREN);
        p1.setTaskName("bar");
        progress.setTaskName("boz");
        reporter.expect("boz");
    }
    
    @Test
    public void sibling() {
        progress.setTaskName("foo");
        Progress p1 = progress.subtask(1, Naming.AS_SIBLINGS);
        p1.setTaskName("bar");
        reporter.expect("bar");
    }

    @Test
    public void items() {
        progress.setTaskName("foo");
        ItemizedProgress items = progress.items(2);
        items.item("abc");
        reporter.expect("foo/abc");
        items.item("def");
        reporter.expect("foo/def");
    }
    
}
