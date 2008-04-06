package com.yoursway.progress.ui.test;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.osgi.service.runnable.ApplicationRunnable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.yoursway.progress.core.Cancellation;
import com.yoursway.progress.core.ItemizedProgress;
import com.yoursway.progress.core.Naming;
import com.yoursway.progress.core.Progress;
import com.yoursway.progress.core.ProgressTracking;

public class MainApp implements ApplicationRunnable {
    
    private static final Point SIZE = new Point(300, 200);
    private Label label;
    private ProgressBar bar;
    private Button stopButton;
    private SwtProgressReporter reporter;
    private Button startButton;
    
    public Object run(Object context) throws Exception {
        Display display = new Display();
        Shell shell = new Shell(display, SWT.SHELL_TRIM);
        shell.setText("Progress test");
        
        startButton = new Button(shell, SWT.NONE);
        startButton.setText("Run a long job");
        startButton.addSelectionListener(new SelectionAdapter() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                execute();
            }
            
        });
        
        stopButton = new Button(shell, SWT.NONE);
        stopButton.setText("Stop");
        stopButton.addSelectionListener(new SelectionAdapter() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                reporter.cancel();
                stopButton.setEnabled(false);
            }
            
        });
        
        label = new Label(shell, SWT.NONE);
        label.setText("");
        
        bar = new ProgressBar(shell, SWT.NONE);
        bar.setMinimum(0);
        bar.setMaximum(100);
        bar.setSelection(10);
        
        GridLayoutFactory.swtDefaults().generateLayout(shell);
        
        shell.setSize(SIZE);
        Rectangle bounds = display.getPrimaryMonitor().getBounds();
        shell.setLocation(bounds.x + (bounds.width - SIZE.x) / 2, bounds.y + (bounds.height - SIZE.y) / 2);
        
        shell.open();
        while (!shell.isDisposed())
            if (!display.readAndDispatch())
                display.sleep();
        return null;
    }
    
    protected void execute() {
        reporter = new SwtProgressReporter(bar, label);
        final Progress progress = ProgressTracking.track(reporter);
        new Thread() {
            
            @Override
            public void run() {
                try {
                    Display.getDefault().asyncExec(new Runnable() {
                        
                        public void run() {
                            startButton.setEnabled(false);
                            stopButton.setEnabled(true);
                            label.setText("Running...");
                        }
                        
                    });
                    execute(progress);
                } catch (Cancellation e) {
                } finally {
                    Display.getDefault().asyncExec(new Runnable() {
                        
                        public void run() {
                            startButton.setEnabled(true);
                            stopButton.setEnabled(false);
                            label.setText("Stopped.");
                        }
                        
                    });
                }
            }
            
        }.start();
    }
    
    private void execute(Progress progress) {
        progress.setTaskName("Downloading updates");
        Progress downloadProgress = progress.subtask(20, Naming.AS_CHILDREN);
//        Progress installProgress = progress.subtask(20, Naming.AS_CHILDREN);
        
        download(downloadProgress);
    }
    
    private void download(Progress progress) {
        int count = 12;
        ItemizedProgress items = progress.items(count);
        for (int i = 0; i < count; i++) {
            if (i % 3 == 1) {
                items.skip();
                continue;
            }
            items.item("file" + i);
            Progress downloadProgress = items.subtask(10, Naming.AS_CHILDREN);
            Progress verifyProgress = items.subtask(1, Naming.AS_CHILDREN);
            String s = download(1, downloadProgress);
            verify(s, verifyProgress);
        }
        items.done();
    }
    
    private void verify(String s, Progress progress) {
        progress.setTaskName("Verification");
        work(progress, 5, 1);
        progress.done();
    }
    
    private String download(int index, Progress progress) {
        progress.setTaskName("Downloading");
        int size = 5 * (10 + index);
        work(progress, size, 5);
        progress.done();
        return "item" + index;
    }

    private void work(Progress progress, int size, int increment) {
        progress.allocate(size);
        for (int i = 0; i < size; i += increment) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progress.worked(increment);
        }
    }
    
    public void stop() {
    }
    
}
