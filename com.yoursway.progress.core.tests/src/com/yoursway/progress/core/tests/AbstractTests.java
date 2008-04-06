package com.yoursway.progress.core.tests;

import org.junit.Before;

import com.yoursway.progress.core.Progress;
import com.yoursway.progress.core.ProgressTracking;
import com.yoursway.progress.core.tests.mocks.ContinuityReporter;

public abstract class AbstractTests {
    
    protected ContinuityReporter reporter;
    protected Progress progress;

    @Before
    public void createReporter() {
        reporter = new ContinuityReporter(1);
        progress = ProgressTracking.track(reporter);
    }

    protected static void work(int steps, Progress p) {
        p.allocate(steps);
        for (int i = 0; i < steps; i++)
            p.worked(1);
    }

}
