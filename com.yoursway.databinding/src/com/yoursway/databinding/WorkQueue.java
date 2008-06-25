package com.yoursway.databinding;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * NON-API - Helper class to manage a queue of runnables to be posted to the UI thread in a way
 * that they are only run once.
 * @since 1.1
 *
 */
public class WorkQueue {
	
    private boolean updateScheduled = false;

    private boolean paintListenerAttached = false;

    private LinkedList pendingWork = new LinkedList();

    private Display d;

    private Set pendingWorkSet = new HashSet();

    private Runnable updateJob = new Runnable() {
        public void run() {
            doUpdate();
            updateScheduled = false;
        }
    };

    private Listener paintListener = new Listener() {
        public void handleEvent(Event event) {
            paintListenerAttached = false;
            d.removeFilter(SWT.Paint, this);
            doUpdate();
        }
    };

    /**
     * @param targetDisplay
     */
    public WorkQueue(Display targetDisplay) {
        d = targetDisplay;
    }

    private void doUpdate() {
        for (;;) {
            Runnable next;
            synchronized (pendingWork) {
                if (pendingWork.isEmpty()) {
                    break;
                }
                next = (Runnable) pendingWork.removeFirst();
                pendingWorkSet.remove(next);
            }

            next.run();
        }
    }

    /**
     * Schedules some work to happen in the UI thread as soon as possible. If
     * possible, the work will happen before the next control redraws. The given
     * runnable will only be run once. Has no effect if this runnable has
     * already been queued for execution.
     * 
     * @param work
     *            runnable to execute
     */
    public void runOnce(Runnable work) {
        synchronized (pendingWork) {
            if (pendingWorkSet.contains(work)) {
                return;
            }

            pendingWorkSet.add(work);

            asyncExec(work);
        }
    }

    /**
     * Schedules some work to happen in the UI thread as soon as possible. If
     * possible, the work will happen before the next control redraws. Unlike
     * runOnce, calling asyncExec twice with the same runnable will cause that
     * runnable to run twice.
     * 
     * @param work
     *            runnable to execute
     */
    public void asyncExec(Runnable work) {
        synchronized (pendingWork) {
            pendingWork.add(work);
            if (!updateScheduled) {
                updateScheduled = true;
                d.asyncExec(updateJob);
            }

            // If we're in the UI thread, add an event filter to ensure
            // the work happens ASAP
            
            // XXX this was way too smart to work - A.T.
//            if (Display.getCurrent() == d) {
//                if (!paintListenerAttached) {
//                    paintListenerAttached = true;
//                    d.addFilter(SWT.Paint, paintListener);
//                }
//            }
        }
    }

    /**
     * Cancels a previously-scheduled runnable. Has no effect if the given
     * runnable was not previously scheduled or has already executed.
     * 
     * @param toCancel
     *            runnable to cancel
     */
    public void cancelExec(Runnable toCancel) {
        synchronized (pendingWork) {
            pendingWork.remove(toCancel);
            pendingWorkSet.remove(toCancel);
        }
    }

    /**
     * Cancels all pending work.
     */
    public void cancelAll() {
        synchronized (pendingWork) {
            pendingWork.clear();
            pendingWorkSet.clear();
        }
    }
}
