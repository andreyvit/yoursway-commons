package com.yoursway.progress.core.tests.mocks;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import com.yoursway.progress.core.ProgressReporter;
import com.yoursway.progress.core.tests.internal.Join;

public class ActionReporter implements ProgressReporter {
    
    private List<String> actions = new ArrayList<String>();
    
    public boolean isCancelled() {
        return false;
    }

    public void setAction(int level, String action) {
        if (level == actions.size())
            actions.add(action);
        else {
            actions.set(level, action);
            while (level < actions.size() - 1)
                actions.remove(actions.size() - 1);
        }
    }

    public void setProgress(double progress) {
    }

    public void expect(String actions) {
        Assert.assertEquals(actions, Join.join("/", this.actions));
    }

}
