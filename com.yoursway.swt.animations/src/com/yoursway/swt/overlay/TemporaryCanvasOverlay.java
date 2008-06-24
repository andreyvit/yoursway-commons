/**
 * 
 */
package com.yoursway.swt.overlay;

import static com.yoursway.swt.additions.YsSwtUtils.lowerLeft;
import static com.yoursway.swt.additions.YsSwtUtils.setLowerLeft;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class TemporaryCanvasOverlay implements Overlay {
    
    private GC gc;
    private Canvas canvas;
    private Image screenshot;
    private Rectangle bounds;
    private Control originalControl;
    
    public TemporaryCanvasOverlay(Control control, Rectangle bounds) {
        if (control == null)
            throw new NullPointerException("control is null");
        if (bounds == null)
            throw new NullPointerException("bounds is null");
        
        this.originalControl = control;
        control = findSuitableControl(control, bounds);
        this.bounds = bounds;
        makeScreenshot(control);
        canvas = new Canvas((Composite) control, SWT.NO_BACKGROUND);
        canvas.moveAbove(null);
        canvas.setBounds(bounds);
        this.gc = new GC(canvas);
        drawOverlayBackgroundOnto(gc, new Rectangle(0, 0, bounds.width, bounds.height));
        canvas.setBackground(new Color(null, 255, 0, 0));
        gc.fillRectangle(new Rectangle(0, 0, bounds.width, bounds.height));
    }
    
    private Control findSuitableControl(Control control, Rectangle bounds) {
        Point screenPoint = control.toDisplay(bounds.x, bounds.y);
        Rectangle controlBounds = control.getBounds();
        while (bounds.x < 0 || bounds.y < 0 || bounds.x + bounds.width > controlBounds.width
                || bounds.y + bounds.height > controlBounds.height) {
            Composite parent = control.getParent();
            if (parent == null)
                break;
            control = parent;
            Point controlPoint = control.toControl(screenPoint);
            bounds.x = controlPoint.x;
            bounds.y = controlPoint.y;
            controlBounds = control.getBounds();
        }
        return control;
    }
    
    private void makeScreenshot(Control control) {
        GC screenshotGc;
        if (control instanceof Shell) {
            Rectangle clientArea = ((Shell) control).getClientArea();
            screenshot = new Image(control.getDisplay(), clientArea.width, clientArea.height);
            Image temp = new Image(control.getDisplay(), clientArea.width, clientArea.height);
            screenshotGc = new GC(screenshot);
            GC tempGc = new GC(temp);
            
            screenshotGc.setBackground(control.getBackground());
            screenshotGc.fillRectangle(clientArea);
            for (Control child : ((Shell) control).getChildren()) {
                child.print(tempGc);
                Rectangle bounds = child.getBounds();
                screenshotGc.drawImage(temp, 0, 0, bounds.width, bounds.height, bounds.x, bounds.y,
                        bounds.width, bounds.height);
            }
            
            tempGc.dispose();
            temp.dispose();
        } else {
            Point screenshotSize = control.getSize();
            screenshot = new Image(control.getDisplay(), screenshotSize.x, screenshotSize.y);
            screenshotGc = new GC(screenshot);
            control.print(screenshotGc);
        }
        
        // remove the control being flipped
        screenshotGc.setBackground(control.getDisplay().getSystemColor(SWT.COLOR_BLACK));
        Rectangle boundsToErase = originalControl.getBounds();
        Point pt = control.toControl(originalControl.getParent().toDisplay(lowerLeft(boundsToErase)));
        setLowerLeft(boundsToErase, pt);
        screenshotGc.fillRectangle(boundsToErase);
        
        screenshotGc.dispose();
        
        if (false) {
            Shell s = new Shell((Shell) null, SWT.DIALOG_TRIM);
            s.setText("Screenshot");
            Rectangle sb = screenshot.getBounds();
            Rectangle rect = s.computeTrim(50, 50, sb.width, sb.height);
            s.setBounds(rect);
            s.open();
            GC gc2 = new GC(s);
            gc2.drawImage(screenshot, 0, 0);
        }
    }
    
    public void dispose() {
        gc.dispose();
        canvas.dispose();
        screenshot.dispose();
    }
    
    public Rectangle getBounds() {
        return bounds;
    }
    
    public void drawOverlayBackgroundOnto(GC gc, Rectangle targetBounds) {
        targetBounds = new Rectangle(targetBounds.x, targetBounds.y, targetBounds.width, targetBounds.height);
        
        if (true) {
            gc.setBackground(new Color(null, 255, 0, 0));
            gc.fillRectangle(targetBounds);
        }
        Rectangle sourceBounds = new Rectangle(bounds.x, bounds.y, targetBounds.width, targetBounds.height);
        Rectangle imageBounds = screenshot.getBounds();
        if (sourceBounds.x + sourceBounds.width > imageBounds.width)
            sourceBounds.width = imageBounds.width - sourceBounds.x;
        if (sourceBounds.y + sourceBounds.height > imageBounds.height)
            sourceBounds.height = imageBounds.height - sourceBounds.y;
        if (sourceBounds.x < imageBounds.x) {
            sourceBounds.width -= (imageBounds.x - sourceBounds.x);
            targetBounds.x += (imageBounds.x - sourceBounds.x);
            sourceBounds.x = imageBounds.x;
        }
        if (sourceBounds.y < imageBounds.y) {
            sourceBounds.height -= (imageBounds.y - sourceBounds.y);
            targetBounds.y += (imageBounds.y - sourceBounds.y);
            sourceBounds.y = imageBounds.y;
        }
        gc.drawImage(screenshot, sourceBounds.x, sourceBounds.y, sourceBounds.width, sourceBounds.height,
                targetBounds.x, targetBounds.y, sourceBounds.width, sourceBounds.height);
    }
    
    public void renderOffscreenImage(Image offscreenImage) {
        gc.drawImage(offscreenImage, 0, 0);
    }
    
}
