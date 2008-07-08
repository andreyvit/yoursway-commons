package com.yoursway.swt.animations.humaneMessage;

import org.eclipse.swt.widgets.Display;

public class HumaneMessages {

    private static HumaneMessageBox currentMessageBox = null;
    
    public static void showMessage(final String message) {
        final HumaneMessageBox newMessageBox = new HumaneMessageBox(Display.getDefault());
        Runnable runnable = new Runnable() {

            public void run() {
                newMessageBox.show(message);
            }
            
        };
        HumaneMessageBox oldMessageBox = currentMessageBox;
        currentMessageBox = newMessageBox;
        if (oldMessageBox == null)
            runnable.run();
        else
            oldMessageBox.dismissQuickly(runnable);
    }
    
}
