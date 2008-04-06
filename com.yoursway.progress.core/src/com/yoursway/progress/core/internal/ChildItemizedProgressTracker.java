package com.yoursway.progress.core.internal;

import com.yoursway.pingback.core.Pingback;
import com.yoursway.progress.core.ItemizedProgress;
import com.yoursway.progress.core.Naming;
import com.yoursway.progress.core.Progress;

public class ChildItemizedProgressTracker implements ItemizedProgress, Tracker {
    
    private final Tracker parent;
    private final int itemCount;
    private final double totalWeight;
    
    private int itemsDone = 0;
    
    private Progress itemProgress = null;
    private double currentItemWeight;

    public ChildItemizedProgressTracker(Tracker parent, int itemCount, double totalWeight) {
        this.parent = parent;
        this.itemCount = itemCount;
        this.totalWeight = totalWeight;
    }

    public void item() {
        item(1);
    }

    public void item(double weight) {
        endItem();
        checkCancellation();
        itemProgress = new ChildProgressTracker(this, weight, Naming.AS_SIBLINGS);
        this.currentItemWeight = weight;
    }

    public void item(String itemName) {
        item(itemName, 1);
    }

    public void item(String itemName, double weight) {
        item(weight);
        // TODO
    }

    public void skip() {
        skip(1);
    }

    public void skip(double weight) {
        endItem();
        parent.skipWorkBecauseOfChild(weight);
        itemsDone++;
    }

    private void endItem() {
        if (isItemStarted()) {
            itemsDone++;
            itemProgress.done();
            itemProgress = null;
        }
    }

    public Progress subtask(double weight, Naming naming) {
        return itemProgress.subtask(weight, naming);
    }

    public Progress subtask() {
        return itemProgress.subtask(currentItemWeight, Naming.AS_SIBLINGS);
    }

    public void willNotRun() {
        if (itemsDone > 0 || isItemStarted())
            throw new IllegalStateException("willNotRun only makes sense when no items have been started");
        parent.skipWorkBecauseOfChild(totalWeight);
    }
    
    private boolean isItemStarted() {
        return itemProgress != null;
    }

    public void done() {
        endItem();
        if (itemsDone != itemCount)
            Pingback.ignorableErrorCondition("Allocated item count is not equal to the used item count.");
    }

    public void checkCancellation() {
        parent.checkCancellation();
    }

    public void childStarting(int index) {
    }

    public void incrementWorkBecauseOfChild(double delta) {
        parent.incrementWorkBecauseOfChild(delta);
    }

    public void skipWorkBecauseOfChild(double delta) {
        parent.skipWorkBecauseOfChild(delta);
    }
    
}
