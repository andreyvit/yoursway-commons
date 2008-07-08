package com.yoursway.swt.animations.humaneMessage;

import static com.yoursway.swt.additions.YsSwtGeometry.centeredRectange;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.yoursway.swt.overlay.TemporaryCanvasOverlay;

public class HumaneMessageBox {
    
    private final Display display;
    private Point gap;
    private Font font;
    private TemporaryCanvasOverlay overlay;
    
    private Image image;
    private Rectangle overlayBounds;
    private boolean rendered = false;
    private boolean disposed = false;
    private PaintListener paintListener;
    private Canvas canvas;
    
    public HumaneMessageBox(Display display) {
        this.display = display;
    }
    
    public synchronized void show(final String message) {
        if (disposed)
            return;
        
        gap = new Point(25, 10);
        font = new Font(display, "Gill Sans", 54, 0);
            
        Shell shell = display.getActiveShell();
        Rectangle clientArea = shell.getClientArea();
        
        GC gc = new GC(shell);
        gc.setFont(font);
        Point extent = gc.textExtent(message);
        int maxWidth = clientArea.width * 2 / 3;
        int width = extent.x + 2 * gap.x;
        if (width > maxWidth)
            width = maxWidth;
        int height = extent.y + 2 * gap.y;
        Rectangle messageArea = centeredRectange(clientArea, width, height);
        
        overlay = new TemporaryCanvasOverlay(shell, messageArea);
        
        overlayBounds = overlay.getBounds();
        image = new Image(display, overlayBounds.width, overlayBounds.height);
        
        canvas = overlay.getCanvas();
        
        paintListener = new PaintListener() {
            
            public void paintControl(PaintEvent e) {
                paintMessage(message);
                overlay.renderOffscreenImage(image);
            }
            
        };
        canvas.addPaintListener(paintListener);
        
        Listener dismissListener = new Listener() {
            
            public void handleEvent(Event event) {
                dismiss();
                display.removeFilter(SWT.MouseMove, this);
                display.removeFilter(SWT.MouseDown, this);
                display.removeFilter(SWT.KeyDown, this);
            }
            
        };
        
        display.addFilter(SWT.MouseMove, dismissListener);
        display.addFilter(SWT.MouseDown, dismissListener);
        display.addFilter(SWT.KeyDown, dismissListener);
    }
    
    void paintMessage(final String message) {
        GC gc = new GC(image);
        
        overlay.drawOverlayBackgroundOnto(gc, image.getBounds());
        
        Color background = display.getSystemColor(SWT.COLOR_DARK_BLUE);
        gc.setBackground(background);
        
        gc.setAlpha(160);
        gc.fillRoundRectangle(0, 0, overlayBounds.width, overlayBounds.height, 30, 30);
        
        gc.setAlpha(255);
        
        gc.setFont(font);
        gc.setForeground(new Color(display, 255, 255, 255));
        gc.drawText(message, gap.x, gap.y, true);
        
        gc.dispose();
        rendered = true;
    }
    
    public synchronized void dismiss() {
        if (disposed)
            return;
        if (!rendered) {
            if (overlay != null)
                overlay.dispose();
        } else {
            canvas.removePaintListener(paintListener);
            overlay.disposeWithFadeout(image, 370);
        }
        disposed = true;
    }
    
    public synchronized void dismissQuickly(Runnable runnable) {
        if (disposed) {
            runnable.run();
            return;
        }
        if (!rendered) {
            if (overlay != null)
                overlay.dispose();
            runnable.run();
        } else {
            canvas.removePaintListener(paintListener);
            overlay.disposeWithFadeout(image, 100);
            display.timerExec(150, runnable);
        }
        disposed = true;
    }
    
}
