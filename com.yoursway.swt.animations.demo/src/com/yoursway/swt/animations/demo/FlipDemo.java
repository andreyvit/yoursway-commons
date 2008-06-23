package com.yoursway.swt.animations.demo;

import org.eclipse.swt.widgets.Display;

public class FlipDemo {

    public static void main(String args[]) {
        try {
            Display display = new Display();
            new FlipDemoShell(display).open();
            while (!display.isDisposed()) {
                if (!display.readAndDispatch()) {
                    if (display.getShells().length == 0)
                        break;
                    display.sleep();
                }
            }
            display.dispose();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
}
