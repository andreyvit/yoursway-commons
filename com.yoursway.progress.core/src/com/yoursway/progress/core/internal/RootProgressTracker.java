package com.yoursway.progress.core.internal;

import static com.yoursway.utils.YsMath.neq;

import com.yoursway.progress.core.Cancellation;
import com.yoursway.progress.core.Naming;
import com.yoursway.progress.core.Progress;
import com.yoursway.progress.core.ProgressReporter;

public class RootProgressTracker implements Tracker {
    
    /**
     * Progress increments less than this will not be reported.
     */
    private static final double SMALLEST_INCREMENT = 1e-4;
    
    private final ProgressReporter reporter;
    
    private double maximum = 1;
    
    private double done = 0;
    
    private double previouslyReported = -1;

    public RootProgressTracker(ProgressReporter reporter) {
        if (reporter == null)
            throw new NullPointerException("reporter == null");
        this.reporter = reporter;
    }
    
    public Progress createProgress() {
        return new ChildProgressTracker(this, 1, Naming.AS_SIBLINGS);
    }

    public void checkCancellation() throws Cancellation {
        if (reporter.isCancelled())
            throw new Cancellation();
    }

    public void childStarting(int index) {
    }

    public void incrementWorkBecauseOfChild(double delta) {
        done += delta;
        update();
    }

    public void skipWorkBecauseOfChild(double delta) {
        maximum -= delta;
        update();
    }
    
    private void update() {
        double value = done / maximum;
        if (neq(value, previouslyReported, SMALLEST_INCREMENT)) {
            reporter.setProgress(value);
            previouslyReported = value;
        }
    }

    public void setTaskNameFromChild(String name, Naming naming) {
        setTaskNameFromChild(name, 0);
    }

    public void setTaskNameFromChild(String name, int level) {
        reporter.setAction(level, name);
    }

}
