package com.yoursway.swt.animations.flip;

import static com.yoursway.utils.Listeners.newListenersByIdentity;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.System.currentTimeMillis;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.yoursway.swt.overlay.Overlay;
import com.yoursway.swt.overlay.OverlayFactory;
import com.yoursway.swt.overlay.TemporaryCanvasOverlayFactory;
import com.yoursway.utils.Listeners;

/**
 * Given two controls with equal positions and sizes, executes a “flip”
 * animation that hides the first control and shows the second one.
 */
public class Flipper {
    
    private static final int FPS = 30;
    private static final double Z_EYE = 1;
    private static final double Z_OBJECT = Z_EYE + 5;
    
    private Control source;
    private Control destination;
    private final Display display;
    private final OverlayFactory overlayFactory;
    private final int iterations;
    private final double ymax;
    
    private transient Listeners<FlipperListener> listeners = newListenersByIdentity();
    
    private Overlay overlay;
    private Image sourceScreenshot;
    private Image destinationScreenshot;
    private Image offscreenImage;
    private GC offscreenGC;
    private double xmax;
    
    public synchronized void addListener(FlipperListener listener) {
        listeners.add(listener);
    }
    
    public synchronized void removeListener(FlipperListener listener) {
        listeners.remove(listener);
    }
    
    public Flipper(Control source, Control destination) {
        this(source, destination, 370);
    }
    
    public Flipper(Control source, Control destination, long duration) {
        this(source, destination, new TemporaryCanvasOverlayFactory(), duration);
    }
    
    public Flipper(Control source, Control destination, OverlayFactory overlayFactory, long duration) {
        if (source == null)
            throw new NullPointerException("source is null");
        if (destination == null)
            throw new NullPointerException("destination is null");
        if (overlayFactory == null)
            throw new NullPointerException("overlayFactory is null");
        if (duration < 0)
            throw new IllegalArgumentException("duration < 0");
        if (duration > 5000)
            throw new IllegalArgumentException("duration > 5000 (artificial limit to protect user)");
        this.source = source;
        this.destination = destination;
        this.overlayFactory = overlayFactory;
        this.iterations = (int) (duration * FPS / 1000);
        this.display = source.getDisplay();
        this.ymax = calculateMaximumRawValueOfY();
        double zmin = Z_OBJECT - 1.0 /* max of Math.sin on 0 .. Pi/2 */;
        this.xmax = 1 * Z_EYE / zmin;
    }
    
    private double calculateMaximumRawValueOfY() {
        double ymax = 0;
        for (int i = 0; i <= iterations; i++) {
            double angle = PI * i / iterations;
            double cosAngle = cos(angle);
            double sinAngle = sin(angle);
            
            double zFar = Z_OBJECT + sinAngle;
            double zNear = Z_OBJECT - sinAngle;
            double y = cosAngle;
            
            double y1pr = y * Z_EYE / zFar;
            double y2pr = y * Z_EYE / zNear;
            ymax = Math.max(ymax, Math.max(y1pr, y2pr));
        }
        return ymax;
    }
    
    public Control getTopControl() {
        return source;
    }
    
    public void flip() {
        sourceScreenshot = capture(source);
        destinationScreenshot = capture(destination);
        Rectangle screenshotBounds = sourceScreenshot.getBounds();
        int canvasOverhangWidth = (int) (screenshotBounds.width * xmax / (1.0 * Z_EYE / Z_OBJECT))
                - screenshotBounds.width;
        
        overlay = overlayFactory.createOverlay(source, new Rectangle(-canvasOverhangWidth / 2, 0,
                screenshotBounds.width + canvasOverhangWidth, screenshotBounds.height));
        
        Rectangle bounds = overlay.getBounds();
        offscreenImage = new Image(display, bounds.width, bounds.height);
        offscreenGC = new GC(offscreenImage);
        
        Control temp = source;
        source = destination;
        destination = temp;
        
        Thread thread = new FlipExecuter(offscreenImage, offscreenGC, bounds);
        thread.setName("Flip animation");
        thread.setDaemon(true);
        thread.start();
    }
    
    private Image capture(Control control) {
        Point screenshotSize = control.getSize();
        Image image = new Image(display, screenshotSize.x, screenshotSize.y);
        GC screenshotGc = new GC(image);
        control.print(screenshotGc);
        screenshotGc.dispose();
        return image;
    }
    
    private final class FlipExecuter extends Thread {
        private final Image offscreenImage;
        private final GC offscreenGC;
        private final Rectangle canvasBounds;
        
        private FlipExecuter(Image offscreenImage, GC offscreenGC, Rectangle canvasBounds) {
            this.offscreenImage = offscreenImage;
            this.offscreenGC = offscreenGC;
            this.canvasBounds = canvasBounds;
        }
        
        @Override
        public void run() {
            try {
                animate();
            } catch (Throwable e) {
                e.printStackTrace(System.err);
                System.err.println("Flip animation failed.");
            }
            
            display.asyncExec(new Runnable() {
                
                public void run() {
                    try {
                        for (FlipperListener listener : listeners)
                            listener.flipped();
                    } finally {
                        sourceScreenshot.dispose();
                        destinationScreenshot.dispose();
                        overlay.dispose();
                    }
                }
                
            });
        }
        
        private void animate() throws InterruptedException {
            double canvasHalfWidth = canvasBounds.width / 2.0;
            double canvasHalfHeight = canvasBounds.height / 2.0;
            
            Rectangle sourceScreenshotBounds = sourceScreenshot.getBounds();
            int screenshotWidth = sourceScreenshotBounds.width;
            int screenshotHeight = sourceScreenshotBounds.height;
            
            Rectangle offscreenImageBounds = offscreenImage.getBounds();
            
            boolean firstHalf = true;
            for (int i = 0; i <= iterations; i++) {
                long start = currentTimeMillis();
                
                double angle = Math.PI * i / iterations;
                double cosAngle = cos(angle);
                double sinAngle = sin(angle);
                
                double zFar = Z_OBJECT + sinAngle;
                double zNear = Z_OBJECT - sinAngle;
                double y = cosAngle;
                double x = 1;
                
                firstHalf = (angle < Math.PI / 2);
                
                double x1pr = (x * Z_EYE / zFar) / xmax * canvasHalfWidth;
                double y1pr = (y * Z_EYE / zFar) / ymax * canvasHalfHeight;
                
                double x2pr = (x * Z_EYE / zNear) / xmax * canvasHalfWidth;
                double y2pr = (y * Z_EYE / zNear) / ymax * canvasHalfHeight;
                
                int xTopLeft = (int) (canvasHalfWidth - x1pr + 0.5);
                int xTopRight = (int) (canvasHalfWidth + x1pr + 0.5);
                int xBottomLeft = (int) (canvasHalfWidth - x2pr + 0.5);
                int xBottomRight = (int) (canvasHalfWidth + x2pr + 0.5);
                
                int yTop = (int) (canvasHalfHeight - y1pr + 0.5);
                int yBottom = (int) (canvasHalfHeight + y2pr + 0.5);
                
                overlay.drawOverlayBackgroundOnto(offscreenGC, offscreenImageBounds);
                
                int minY = Math.min(yBottom, yTop);
                int maxY = Math.max(yBottom, yTop);
                for (int projY = minY; projY < maxY; projY++) {
                    double factor = ((double) projY - yBottom) / (yTop - yBottom);
                    double yt1 = projY;
                    double yt2 = projY + 1;
                    
                    double factor1 = (yt1 - yBottom) / (yTop - yBottom);
                    double factor2 = (yt2 - yBottom) / (yTop - yBottom);
                    
                    Image currentScreenshot = (firstHalf ? sourceScreenshot : destinationScreenshot);
                    if (firstHalf) {
                        factor1 = 1 - factor1;
                        factor2 = 1 - factor2;
                    }
                    double ysrc1c = screenshotHeight * factor1;
                    double ysrc2c = screenshotHeight * factor2;
                    
                    int projXLeft = (int) ((xTopLeft - xBottomLeft) * factor + xBottomLeft + 0.5);
                    int projXRight = (int) ((xTopRight - xBottomRight) * factor + xBottomRight + 0.5);
                    
                    double ysrc1 = Math.min(ysrc1c, ysrc2c);
                    double ysrc2 = Math.max(ysrc1c, ysrc2c);
                    int yy1 = (int) Math.ceil(ysrc1);
                    int yy2 = (int) (ysrc2 - 0.00001);
                    if (yy2 < yy1)
                        continue;
                    offscreenGC.drawImage(currentScreenshot, 0, yy1, screenshotWidth, yy2 - yy1 + 1,
                            projXLeft, projY, projXRight - projXLeft + 1, 1);
                }
                
                overlay.renderOffscreenImage(offscreenImage);
                
                long span = (int) (System.currentTimeMillis() - start);
                long delay = 1000 / FPS - span;
                Thread.sleep(delay > 0 ? delay : 0);
            }
        }
    }
    
}
