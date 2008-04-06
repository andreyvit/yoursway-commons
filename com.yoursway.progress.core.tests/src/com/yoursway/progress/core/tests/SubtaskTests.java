package com.yoursway.progress.core.tests;

import org.junit.Test;

import com.yoursway.progress.core.Naming;
import com.yoursway.progress.core.Progress;

public class SubtaskTests extends AbstractTests {

    @Test
    public void twoSubTasks() {
        reporter.setSteps(20);
        Progress p1 = progress.subtask(1, Naming.AS_CHILDREN);
        Progress p2 = progress.subtask(1, Naming.AS_CHILDREN);
        work(10, p1);
        work(10, p2);
        reporter.verify();
    }
    
    @Test
    public void cancelledSubTask() {
        Progress p1 = progress.subtask(1, Naming.AS_CHILDREN);
        Progress p2 = progress.subtask(1, Naming.AS_CHILDREN);
        Progress p3 = progress.subtask(1, Naming.AS_CHILDREN);
        reporter.setSteps(30);
        work(10, p1);
        p2.willNotRun();
        reporter.setSteps(10, 0.6667);
        work(10, p3);
        reporter.verify();
    }
    
    @Test
    public void twoSubSubTasks() {
        reporter.setSteps(20);
        Progress p1 = progress.subtask(1, Naming.AS_CHILDREN);
        Progress p2 = progress.subtask(1, Naming.AS_CHILDREN);
        Progress p11 = p1.subtask(2, Naming.AS_CHILDREN);
        Progress p12 = p1.subtask(2, Naming.AS_CHILDREN);
        work(10, p11);
        work(10, p12);
        work(10, p2);
        reporter.verify();
    }

}
