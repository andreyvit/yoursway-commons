package com.yoursway.progress.core.internal;

import static com.yoursway.utils.YsMath.eq;
import static com.yoursway.utils.YsMath.gt;
import static com.yoursway.utils.YsMath.lt;

import com.yoursway.pingback.core.Pingback;
import com.yoursway.progress.core.Cancellation;
import com.yoursway.progress.core.ItemizedProgress;
import com.yoursway.progress.core.Naming;
import com.yoursway.progress.core.Progress;

public class ChildProgressTracker implements Progress, Tracker {

    private static final double LOCAL_EPS = 1e-6;

    private final Tracker parent;
    
    private double allocatedWeight = 0;
    
    private double usedWeight = 0;
    
    private final double parentWeightAllocated;
    
    private double parentWeightUsed = 0;
    
    private final Naming parentNaming;

    private double parentEps;
    
    public ChildProgressTracker(Tracker parent, double parentWeightAllocated, Naming parentNaming) {
        if (parent == null)
            throw new NullPointerException("parent == null");
        this.parent = parent;
        this.parentWeightAllocated = parentWeightAllocated;
        this.parentNaming = parentNaming;
        this.parentEps = parentWeightAllocated / 1000;
    }
    
    public void allocate(double totalWeigth) {
        checkCancellation();
        allocatedWeight += totalWeigth;
    }
    
    public void checkCancellation() throws Cancellation {
        parent.checkCancellation();
    }
    
    public void childStarting(int index) {
    }
    
    public void done() {
        internalDone();
    }

    private void internalDone() {
        if (usedWeight < allocatedWeight)
            internalWorked(allocatedWeight - usedWeight);
        if (parentWeightUsed < parentWeightAllocated)
            parent.incrementWorkBecauseOfChild(parentWeightAllocated - parentWeightUsed);
    }
    
    public void incrementWorkBecauseOfChild(double delta) {
        internalWorked(delta);
    }
    
    public ItemizedProgress items(int items) {
        return items(items, items);
    }
    
    public ItemizedProgress items(int items, double totalWeigth) {
        checkCancellation();
        allocatedWeight += totalWeigth;
        return new ChildItemizedProgressTracker(this, items, totalWeigth);
    }

    public void setTaskName(String name) {
        checkCancellation();
    }
    
    public void skip(double weight) {
        checkCancellation();
        internalSkip(weight);
    }

    public void skipWorkBecauseOfChild(double delta) {
        internalSkip(delta);
    }
    
    public Progress subtask(double weight, Naming naming) {
        checkCancellation();
        allocatedWeight += weight;
        return new ChildProgressTracker(this, weight, naming);
    }
    
    public void willNotRun() {
        checkCancellation();
        parent.skipWorkBecauseOfChild(parentWeightAllocated);
    }
    
    public void worked(double weight) {
        checkCancellation();
        internalWorked(weight);
    }
    
    private void internalSkip(double weight) {
        if (allocatedWeight - usedWeight < weight) {
            Pingback.ignorableErrorCondition("Progress.skip(" + weight
                    + ") requests to skip more than available (" + (allocatedWeight - usedWeight) + " = "
                    + allocatedWeight + "-" + usedWeight + ")");
            weight = allocatedWeight - usedWeight;
        }
        allocatedWeight -= weight;
        if (eq(allocatedWeight, 0, parentEps))
            internalDone();
    }
    
    private void internalWorked(double weight) {
        if (lt(usedWeight + weight, allocatedWeight, LOCAL_EPS))
            usedWeight += weight;
        else
            usedWeight = allocatedWeight;
        updateParentProgress();
    }

    private void updateParentProgress() {
        double ratio = usedWeight / allocatedWeight;
        double parentUnused = parentWeightAllocated - parentWeightUsed;
        double parentDelta = parentUnused * ratio;
        if (gt(parentDelta, 0, parentEps)) {
            parent.incrementWorkBecauseOfChild(parentDelta);
            allocatedWeight -= usedWeight;
            usedWeight = 0;
            parentWeightUsed += parentDelta;
        }
    }
    
}
