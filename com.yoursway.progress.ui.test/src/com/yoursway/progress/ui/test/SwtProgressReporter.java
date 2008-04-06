package com.yoursway.progress.ui.test;

import static com.yoursway.progress.ui.test.Join.join;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

import com.yoursway.progress.core.ProgressReporter;

public class SwtProgressReporter implements ProgressReporter {
    
    private final ProgressBar bar;
    private final Label actionLabel;
    private volatile boolean cancelled;
    
    private List<String> actions = new ArrayList<String>();

    public SwtProgressReporter(ProgressBar bar, Label action) {
        this.bar = bar;
        this.actionLabel = action;
        bar.setMinimum(0);
        bar.setMaximum(10000);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setAction(int level, String action) {
        if (level == actions.size())
            actions.add(action);
        else {
            actions.set(level, action);
            while (level < actions.size() - 1)
                actions.remove(actions.size() - 1);
        }
        bar.getDisplay().asyncExec(new Runnable() {

            public void run() {
                actionLabel.setText(join(" :: ", actions));
            }
            
        });
    }

    public void setProgress(final double progress) {
        final int value = (int) (progress * 10000 + 0.5);
        bar.getDisplay().asyncExec(new Runnable() {

            public void run() {
                bar.setSelection(value);
            }
            
        });
    }

    public void cancel() {
        cancelled = true;
    }
    
}
