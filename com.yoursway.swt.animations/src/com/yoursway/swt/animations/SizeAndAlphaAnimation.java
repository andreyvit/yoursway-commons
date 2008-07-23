package com.yoursway.swt.animations;

import java.lang.Thread.State;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.yoursway.utils.annotations.UseFromAnyThread;

public class SizeAndAlphaAnimation {
    
    private SizeAndAlphaAnimationApplier updater;
    private boolean disposed = false;
    
    private volatile int targetWidth = 0;
    private volatile int targetHeight = 0;
    private volatile int targetAlpha = 0;
    
    private volatile boolean instantWidth = false;
    private volatile int width = 0;
    private volatile int height = 0;
    private volatile int alpha = 0;
    
    private int xDelay = 0;
    private int yDelay = 0;
    
    private static final int d = 2;
    private static final int delay = 10;
    
    private static final Collection<SizeAndAlphaAnimation> animations = new ConcurrentLinkedQueue<SizeAndAlphaAnimation>();
    private static final Thread thread = new Thread(SizeAndAlphaAnimation.class.getSimpleName()) {
        
        @Override
        public void run() {
            try {
                while (true) {
                    Iterator<SizeAndAlphaAnimation> it = animations.iterator();
                    while (it.hasNext()) {
                        SizeAndAlphaAnimation animation = it.next();
                        
                        if (animation.isDisposed()) {
                            animation.updater = null;
                            it.remove();
                            continue;
                        }
                        
                        animation.updateSize();
                        animation.updateAlpha();
                    }
                    sleep(10);
                }
            } catch (InterruptedException e) {
                interrupted();
            }
        }
        
    };
    
    public void start(final SizeAndAlphaAnimationApplier updater) {
        this.updater = updater;
        
        animations.add(this);
        
        if (thread.getState() == State.NEW) {
            thread.setDaemon(true);
            thread.start();
        }
    }
    
    private void updateSize() {
        if (!updater.visible()) {
            width = targetWidth;
            height = targetHeight;
            updater.updateSize(width, height);
            return;
        }
        
        int x = f(targetWidth - width, d);
        if (x < 0) {
            if (xDelay < delay) {
                xDelay++;
                x = 0;
            }
        } else {
            xDelay = 0;
        }
        
        int y = f(targetHeight - height, d);
        if (y < 0) {
            if (yDelay < delay) {
                yDelay++;
                y = 0;
            }
        } else {
            yDelay = 0;
        }
        
        if (x != 0 || y != 0) {
            synchronized (SizeAndAlphaAnimation.this) {
                if (instantWidth)
                    instantWidth = false;
                else
                    width += x;
            }
            
            height += y;
            
            updater.updateSize(width, height);
        }
    }
    
    private void updateAlpha() {
        if (!updater.visible()) {
            alpha = targetAlpha;
            updater.updateAlpha(alpha);
            return;
        }
        
        int a = f(targetAlpha - alpha, d * 3);
        if (a != 0) {
            alpha += a;
            updater.updateAlpha(alpha);
        }
    }
    
    @UseFromAnyThread
    public void targetSize(int width, int height) {
        targetWidth = width;
        targetHeight = height;
    }
    
    @UseFromAnyThread
    public void targetAlpha(int alpha) {
        targetAlpha = alpha;
    }
    
    private int f(int x, int d) {
        if (x == 0)
            return 0;
        int f = x / d;
        if (f == 0)
            return x > 0 ? 1 : -1;
        return f;
    }
    
    public void instantWidth() {
        updater.updateSize(targetWidth, height);
        synchronized (this) {
            width = targetWidth;
            instantWidth = true;
        }
    }
    
    public void dispose() {
        if (isDisposed())
            return;
        
        disposed = true;
    }
    
    public boolean isDisposed() {
        return disposed;
    }
    
}
