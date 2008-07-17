package com.yoursway.swt.inspector.demo;

import static com.yoursway.swt.additions.YsSwtUtils.centerShellOnNearestMonitor;
import static com.yoursway.swt.inspector.SwtInspector.showSwtInspector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class SwtInspectorDemo {
    
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new GridLayout(1, false));
        
        Composite composite1 = new Composite(shell, SWT.NONE);
        composite1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite1.setLayout(new GridLayout(1, false));
        
        new Button(composite1, SWT.PUSH).setText("Sample button in C1");
        
        new Label(composite1, SWT.NONE).setText("Sample label in C1");
        
        new Button(composite1, SWT.CHECK).setText("Sample checkbox in C1");
        
        Composite composite2 = new Composite(shell, SWT.NONE);
        composite2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite2.setLayout(new GridLayout(1, false));
        
        new Button(composite2, SWT.PUSH).setText("Sample button in C1");
        
        new Label(composite2, SWT.NONE).setText("Sample label in C1");
        
        new Button(composite2, SWT.CHECK).setText("Sample checkbox in C1");

        shell.setSize(600, 400);
        centerShellOnNearestMonitor(shell);
        shell.open();
        
        showSwtInspector();
        
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
    
}
