package com.yoursway.swt.additions;

import static com.yoursway.swt.additions.YsStandardFonts.miniFont;
import static com.yoursway.swt.additions.YsStandardFonts.smallFont;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
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
    
    public static Point lowerLeft(Rectangle rectangle) {
        return new Point(rectangle.x, rectangle.y);
    }
    
    public static void setLowerLeft(Rectangle rectangle, Point point) {
        rectangle.x = point.x;
        rectangle.y = point.y;
    }
    
    public static Display currentDisplay() {
        Display display = Display.getCurrent();
        if (display == null)
            throw new IllegalStateException("Must be called from a UI thread");
        return display;
    }
    
    public static void applyMiniSize(Control control) {
        applyFont(control, miniFont());
    }
    
    public static void applySmallSize(Control control) {
        applyFont(control, smallFont());
    }
    
    public static void applyFont(Control control, Font font) {
        control.setFont(font);
        if (control instanceof Composite)
            for (Control child : ((Composite) control).getChildren())
                applyFont(child, font);
    }
    
}
