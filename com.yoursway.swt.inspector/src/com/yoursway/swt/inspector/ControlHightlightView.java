package com.yoursway.swt.inspector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ControlHightlightView {
    
    private final Display display;
    private GC gc;
    private Control highlightedControl;

    public ControlHightlightView(Display display) {
        if (display == null)
            throw new NullPointerException("display is null");
        this.display = display;
    }

    public void highlightDisplayArea(Rectangle bounds) {
        if (gc != null)
            gc.dispose();
        gc = new GC(display);
        gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
        gc.drawRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
        gc.drawRectangle(bounds.x - 1, bounds.y - 1, bounds.width + 2, bounds.height + 2);
    }
    
    public void removeHightlight(Control control) {
        if (highlightedControl == control)
            dehighlight();
    }

    public void dehighlight() {
        if (gc != null)
            gc.dispose();
        gc = null;
        highlightedControl = null;
    }

    public void highlight(Control control) {
        if (control == highlightedControl)
            return;
        
        dehighlight();
        
        gc = new GC(display);
        highlightedControl = control;
        
        highlightParents(control.getParent());
        draw(calculateDisplayBoundsOf(control), SWT.COLOR_RED);
    }

    private void highlightParents(Composite parent) {
        if (parent == null || parent instanceof Shell)
            return;
        highlightParents(parent.getParent());
        draw(calculateDisplayBoundsOf(parent), SWT.COLOR_GREEN);
    }

    private void draw(Rectangle bounds, int color) {
        gc.setForeground(display.getSystemColor(color));
        gc.drawRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
        gc.drawRectangle(bounds.x - 1, bounds.y - 1, bounds.width + 2, bounds.height + 2);
    }

    private Rectangle calculateDisplayBoundsOf(Control control) {
        Rectangle bounds = control.getBounds();
        if (!(control instanceof Shell))
            bounds = display.map(control.getParent(), null, bounds);
        return bounds;
    }

}
