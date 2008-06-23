package com.yoursway.swt.additions;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class YsSwtUtils {
    
    public static void centerShellOnNearestMonitor(Shell shell) {
        Monitor nearestMonitor = shell.getMonitor();
        Rectangle monitorBounds = nearestMonitor.getBounds();
        Point shellSize = shell.getSize();
        shell.setLocation(monitorBounds.x + (monitorBounds.width - shellSize.x) / 2, monitorBounds.y
                + (monitorBounds.height - shellSize.y) / 2);
    }
    
}
