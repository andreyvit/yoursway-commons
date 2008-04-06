package com.yoursway.progress.ui.test;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

import com.yoursway.progress.core.ProgressReporter;

public class SwtProgressReporter implements ProgressReporter {
    
    private final ProgressBar bar;
    private final Label action;
    private volatile boolean cancelled;

    public SwtProgressReporter(ProgressBar bar, Label action) {
        this.bar = bar;
        this.action = action;
        bar.setMinimum(0);
        bar.setMaximum(10000);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setAction(String action) {
        this.action.setText(action);
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
