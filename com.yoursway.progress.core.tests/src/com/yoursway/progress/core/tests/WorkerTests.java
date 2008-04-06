package com.yoursway.progress.core.tests;

import org.junit.Test;

import com.yoursway.progress.core.tests.mocks.ContinuityException;
import com.yoursway.progress.core.tests.mocks.FinishException;

public class WorkerTests extends AbstractTests {

    @Test
    public void allocateWorked() {
        reporter.setSteps(40);
        work(40, progress);
        reporter.verify();
    }
    
    @Test(expected=ContinuityException.class)
    public void allocateWorkedBigStep() {
        reporter.setSteps(40);
        work(20, progress);
        reporter.verify();
    }
    
    @Test(expected=FinishException.class)
    public void allocateWorkedNotEnoughSteps() {
        reporter.setSteps(40);
        progress.allocate(40);
        for (int i = 0; i < 20; i++)
            progress.worked(1);
        reporter.verify();
    }
    
}
