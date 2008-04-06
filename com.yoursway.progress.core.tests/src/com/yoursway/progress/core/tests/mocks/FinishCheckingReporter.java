package com.yoursway.progress.core.tests.mocks;

import static com.yoursway.utils.YsMath.neq;

import com.yoursway.progress.core.ProgressReporter;

public class FinishCheckingReporter implements ProgressReporter {
    
    protected static final double EPS = 1e-6;

    private double previousProgress = 0;
    
    protected boolean verified = false;
    
    public boolean isCancelled() {
        return false;
    }

    public void setAction(int level, String action) {
    }

    public void setProgress(double progress) {
        double prev = previousProgress;
        String stepDesc = prev + " -> " + progress;
        verifyStep(progress, prev, stepDesc);
        previousProgress = progress;
    }

    protected void verifyStep(double progress, double prev, String stepDesc) {
    }
    
    public void verify() {
        verified = true;
        if (neq(previousProgress, 1, EPS))
            throw new FinishException("Progress did not finish on 1.0: " + previousProgress);
    }
    
    public void expect(double value) {
        if (neq(previousProgress, value, EPS))
            throw new FinishException("Progress expected: " + value + ", actual: " + previousProgress);
    }

}
