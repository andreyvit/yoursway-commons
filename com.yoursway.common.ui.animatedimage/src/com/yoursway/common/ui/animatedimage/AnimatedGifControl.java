package com.yoursway.common.ui.animatedimage;

import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Andrey Tarantsov
 * @see http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/DisplayananimatedGIF.htm
 */
public class AnimatedGifControl extends Composite implements Listener {
    
    final static class AnimatedGifPainter {
        
        private final Display display;
        
        private Image canvasImage = null;
        private GC canvasGC = null;
        
        private Image currentFrameImage = null;
        
        private ImageLoader loader;
        
        private int frameOnOffscreenImage = -1;
        
        private Image backgroundImage;
        
        private Point backgroundImageOffset;
        
        private Color backgroundColor;
        
        public AnimatedGifPainter(Display display) {
            if (display == null)
                throw new NullPointerException("display is null");
            this.display = display;
        }
        
        public void setImageData(ImageLoader loader) {
            this.loader = loader;
            disposeCanvas();
            canvasImage = new Image(display, loader.logicalScreenWidth, loader.logicalScreenHeight);
            canvasGC = new GC(canvasImage);
        }
        
        public void setBackgroundColor(Color color) {
            if (color == null)
                throw new NullPointerException("color is null");
            backgroundColor = color;
            backgroundImage = null;
            backgroundImageOffset = null;
        }
        
        public void setBackgroundImage(Image image, Point offset) {
            if (image == null)
                throw new NullPointerException("image is null");
            if (offset == null)
                throw new NullPointerException("offset is null");
            backgroundColor = null;
            backgroundImage = image;
            backgroundImageOffset = offset;
        }
        
        public void setCurrentImage(int index) {
            disposeFrameImage();
            
            if (frameOnOffscreenImage >= 0) {
                ImageData data = loader.data[frameOnOffscreenImage];
                switch (data.disposalMethod) {
                case SWT.DM_FILL_BACKGROUND:
                    fillCanvasWithBackground();
                    break;
                case SWT.DM_FILL_PREVIOUS:
                    // TODO implement me
                    throw new UnsupportedOperationException(
                            "DM_FILL_PREVIOUS not supported in animated Gifs, but can be easily added (pls see code)");
                case SWT.DM_FILL_NONE:
                    break;
                }
            } else {
                fillCanvasWithBackground();
            }
            
            ImageData imageData = loader.data[index];
            currentFrameImage = new Image(display, imageData);
            canvasGC.drawImage(currentFrameImage, 0, 0, imageData.width, imageData.height, imageData.x,
                    imageData.y, imageData.width, imageData.height);
            frameOnOffscreenImage = index;
        }
        
        private void fillCanvasWithBackground() {
            if (backgroundImage != null)
                canvasGC.drawImage(backgroundImage, backgroundImageOffset.x, backgroundImageOffset.y,
                        loader.logicalScreenWidth, loader.logicalScreenHeight, 0, 0,
                        loader.logicalScreenWidth, loader.logicalScreenHeight);
            else {
                //              Color bgColor = null;
                //              if (useGifBackground && loader.backgroundPixel != -1) {
                //                  bgColor = new Color(getDisplay(), imageData.palette
                //                          .getRGB(loader.backgroundPixel));
                //              }
                //              if (bgColor != null)
                //                  bgColor.dispose();
                canvasGC.setBackground(backgroundColor);
                canvasGC.fillRectangle(0, 0, loader.logicalScreenWidth, loader.logicalScreenHeight);
            }
        }
        
        private void disposeCanvas() {
            if (canvasGC != null)
                canvasGC.dispose();
            if (canvasImage != null)
                canvasImage.dispose();
        }
        
        private void disposeFrameImage() {
            if (currentFrameImage != null && !currentFrameImage.isDisposed())
                currentFrameImage.dispose();
        }
        
        public int frameCount() {
            return loader.data.length;
        }
        
        public int frameWidth() {
            return loader.logicalScreenWidth;
        }
        
        public int frameHeight() {
            return loader.logicalScreenHeight;
        }
        
        public void paintOn(GC gc, int x, int y) {
            gc.drawImage(canvasImage, x, y);
        }
        
        public void dispose() {
            disposeCanvas();
            disposeFrameImage();
        }
        
    }
    
    private final class AnimationThread extends Thread {
        
        private AnimationThread(String name) {
            super(name);
        }
        
        public void run() {
            AnimatedGifPainter painter = new AnimatedGifPainter(getDisplay());
            try {
                SharedData data = null;
                boolean shouldRunAnimation = false;
                while (!shouldRunAnimation)
                    synchronized (sharedDataSync) {
                        data = sharedData;
                        shouldRunAnimation = data.backgroundImageValid && data.imageLoaded && data.isVisible
                                && data.clientArea != null;
                        if (!shouldRunAnimation)
                            try {
                                sharedDataSync.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                    }
                painter.setImageData(loader);
                painter.setBackgroundImage(data.backgroundImage, new Point(data.bounds.x, data.bounds.y));
                int imageDataIndex = 0;
                painter.setCurrentImage(imageDataIndex);
                int repeatCount = loader.repeatCount;
                while (loader.repeatCount == 0 || repeatCount > 0) {
                    if (isDisposed())
                        break;
                    
                    imageDataIndex = (imageDataIndex + 1) % painter.frameCount();
                    painter.setCurrentImage(imageDataIndex);
                    
                    /* Draw the off-screen image to the shell. */
                    int x = (data.clientArea.width - painter.frameWidth()) / 2;
                    int y = (data.clientArea.height - painter.frameHeight()) / 2;
                    painter.paintOn(gc, x, y);
                    
                    try {
                        sleepWithFudge(data.delayOverrideMs >= 0 ? data.delayOverrideMs
                                : loader.data[imageDataIndex].delayTime * 10);
                    } catch (InterruptedException e) {
                        return;
                    }
                    
                    if (imageDataIndex == painter.frameCount() - 1)
                        repeatCount--;
                }
            } catch (SWTException ex) {
                if (ex.code != SWT.ERROR_WIDGET_DISPOSED) {
                    System.out.println("There was an error animating the GIF");
                    ex.printStackTrace();
                }
            } finally {
                painter.dispose();
            }
        }
    }
    
    /**
     * Sleep for the specified delay time (adding commonly-used slow-down fudge
     * factors).
     */
    static void sleepWithFudge(int ms) throws InterruptedException {
        if (ms < 20)
            ms += 30;
        if (ms < 30)
            ms += 10;
        Thread.sleep(ms);
    }
    
    private static class SharedData implements Cloneable {
        
        public boolean useGifBackground = false;
        
        public boolean imageLoaded = false;
        
        public int delayOverrideMs = -1;
        
        public boolean backgroundImageValid = false;
        
        public Image backgroundImage;
        
        public GC backgroundGC;
        
        public Rectangle clientArea;
        
        public boolean isVisible;
        
        public Rectangle bounds;
        
        @Override
        protected SharedData clone() {
            try {
                return (SharedData) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(e);
            }
        }
        
    }
    
    private volatile SharedData sharedData = new SharedData();
    
    private final Object sharedDataSync = new Object();
    
    private final GC gc;
    
    private Thread animateThread;
    
    private final ImageLoader loader;
    
    public AnimatedGifControl(Composite parent, int style) {
        super(parent, style);
        gc = new GC(this);
        loader = new ImageLoader();
        
        animateThread = new AnimationThread("Animation");
        animateThread.setDaemon(true);
        animateThread.start();
        
        addListener(SWT.Resize, this);
        addListener(SWT.Show, this);
        addListener(SWT.Hide, this);
        handleResize();
    }
    
    public void loadImage(InputStream stream) {
        SharedData newData = sharedData.clone();
        try {
            loader.load(stream);
            newData.imageLoaded = true;
        } catch (SWTException ex) {
            System.out.println("There was an error loading the GIF");
            ex.printStackTrace();
            newData.imageLoaded = false;
        }
        setSharedData(newData);
    }
    
    @Override
    public Point computeSize(int wHint, int hHint, boolean changed) {
        if (sharedData.imageLoaded)
            return new Point(loader.logicalScreenWidth, loader.logicalScreenHeight);
        else
            return super.computeSize(wHint, hHint, changed);
    }
    
    public void setDelayOverrideMs(int delayOverrideMs) {
        SharedData newData = sharedData.clone();
        newData.delayOverrideMs = delayOverrideMs;
        setSharedData(newData);
        updateVisibility();
    }
    
    public void handleEvent(Event event) {
        switch (event.type) {
        case SWT.Show:
            photocopyBackground();
            updateVisibility();
            break;
        case SWT.Hide:
            updateVisibility();
            break;
        case SWT.Resize:
            handleResize();
            break;
        }
    }
    
    private void handleResize() {
        SharedData newData = sharedData.clone();
        newData.clientArea = getClientArea();
        setSharedData(newData);
    }
    
    private void updateVisibility() {
        SharedData data = sharedData.clone();
        data.isVisible = isVisible();
        if (!data.isVisible)
            data.backgroundImageValid = false;
        setSharedData(data);
    }
    
    public void photocopyBackground() {
        Composite parent = getParent();
        Rectangle clientArea = parent.getClientArea();
        SharedData newData = sharedData.clone();
        newData.backgroundImage = new Image(getDisplay(), clientArea.width, clientArea.height);
        newData.backgroundGC = new GC(newData.backgroundImage);
        parent.print(newData.backgroundGC);
        newData.backgroundImageValid = true;
        newData.bounds = getBounds();
        setSharedData(newData);
    }
    
    private void setSharedData(SharedData sharedData) {
        synchronized (sharedDataSync) {
            this.sharedData = sharedData;
            sharedDataSync.notifyAll();
        }
    }
    
}
