package com.yoursway.progress.core.tests.mocks;

import static com.yoursway.utils.YsMath.gt;
import static com.yoursway.utils.YsMath.lt;

public class ContinuityReporter extends FinishCheckingReporter {
    
    private double validStep;
    
    public ContinuityReporter(int steps) {
        setSteps(steps);
    }

    public void setSteps(int steps) {
        this.validStep = 1.0 / steps;
    }
    
    public void setSteps(int steps, double span) {
        this.validStep = span / steps;
    }

    protected void verifyStep(double progress, double prev, String stepDesc) {
        if (lt(progress, prev, EPS))
            throw new MonotonessException("Progress must be monotonically increasing: " + stepDesc);
        if (gt(Math.abs(prev - progress), validStep, 1e-6))
            throw new ContinuityException("The step is too big: " + stepDesc + 
                    ", step " + Math.abs(prev - progress) + ", max " + validStep);
    }
    
}
