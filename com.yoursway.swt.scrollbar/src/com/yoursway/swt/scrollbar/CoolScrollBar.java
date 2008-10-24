package com.yoursway.swt.scrollbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import com.yoursway.utils.annotations.SynchronizedWithMonitorOfField;

public class CoolScrollBar extends Canvas {
    
    private float whole;
    private float visibleHeight;
    private float position;
    
    private float alpha;
    private final Color color;
    
    private float beginMargin;
    private float endMargin;
    
    private boolean vertical = true;
    
    @SynchronizedWithMonitorOfField("animationLock")
    private boolean runningAnimation = false;
    
    private Thread animationThread;
    private final AnimationRunnable animationRunnable;
    private final Object animationLock = new Object();
    protected Rectangle lastRunnerRect;
    
    private class DragginMouseListener implements MouseListener, MouseMoveListener {
        
        private boolean dragMode;
        private float mouseOffset;
        
        public void mouseDoubleClick(MouseEvent e) {
        }
        
        public void mouseDown(MouseEvent e) {
            if (lastRunnerRect != null) {
                dragMode = true;
                if (!lastRunnerRect.contains(e.x, e.y)) {
                    //> scrolling by pages 
                    float newPosition = positionForClick(e.x, e.y);
                    if (newPosition >= 0) {
                        position = newPosition;
                        redraw();
                        CoolScrollBar.this.notifyListeners(SWT.Selection, new Event());
                    }
                }
                mouseOffset = e.y - clickForPosition(position);
            }
        }
        
        public void mouseUp(MouseEvent e) {
            dragMode = false;
        }
        
        public void mouseMove(MouseEvent e) {
            if (dragMode) {
                float newPosition = positionForClick(e.x - mouseOffset, e.y - mouseOffset);
                if (newPosition >= 0) {
                    position = newPosition;
                    redraw();
                    CoolScrollBar.this.notifyListeners(SWT.Selection, new Event());
                }
            }
        }
        
        private float positionForClick(float xc, float yc) {
            float coord = (vertical ? yc : xc) - runnerLength() / 2;
            
            if (ratio() > 0.9) // nothing to show
                return -1;
            
            float activeSpace = activeSpace();
            
            if (coord < beginMargin)
                coord = beginMargin;
            
            if (coord > beginMargin + activeSpace)
                coord = beginMargin + activeSpace;
            
            return (whole - visibleHeight) * (coord - beginMargin) / activeSpace;
        }
        
        private float clickForPosition(float position) {
            return position * activeSpace() / (whole - visibleHeight) + beginMargin + runnerLength() / 2;
        }
        
        private float activeSpace() {
            return length() - runnerLength() - beginMargin - endMargin;
        }
        
        private float runnerLength() {
            return Math.max(ratio() * length(), 10);
        }
        
        private float length() {
            Rectangle clientArea = CoolScrollBar.this.getClientArea();
            return vertical ? clientArea.height : clientArea.width;
        }
        
        private float ratio() {
            return visibleHeight / whole;
        }
        
    }
    
    private Rectangle calculateRunnerRect() {
        float ratio = visibleHeight / whole;
        if (ratio > 0.9) // nothing to show
            return null;
        Rectangle clientArea = CoolScrollBar.this.getClientArea();
        int length = vertical ? clientArea.height : clientArea.width;
        int runnerLength = (int) Math.max(ratio * length, 10);
        int activeSpace = (int) (length - runnerLength - beginMargin - endMargin);
        int pos = (int) (activeSpace * (position / (whole - visibleHeight)) + beginMargin);
        if (vertical)
            return new Rectangle(3, pos, 7, runnerLength);
        else
            return new Rectangle(pos, 3, runnerLength, 7);
    }
    
    public CoolScrollBar(Composite parent, int style, boolean vertical) {
        this(parent, style, vertical, new Color(Display.getDefault(), 80, 80, 80));
        addDisposeListener(new DisposeListener() {
            
            public void widgetDisposed(DisposeEvent e) {
                color.dispose();
            }
            
        });
    }
    
    public CoolScrollBar(Composite parent, int style, boolean vertical, Color color) {
        super(parent, style | SWT.DOUBLE_BUFFERED);
        this.vertical = vertical;
        alpha = 0;
        beginMargin = 6;
        endMargin = 6;
        this.color = color;
        this.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        addPaintListener(new PaintListener() {
            
            public void paintControl(PaintEvent e) {
                Rectangle runnerRect = calculateRunnerRect();
                if (runnerRect != null) {
                    GC gc = e.gc;
                    gc.setAlpha((int) (alpha * 255));
                    gc.setBackground(CoolScrollBar.this.color);
                    gc.fillRoundRectangle(runnerRect.x, runnerRect.y, runnerRect.width, runnerRect.height, 7,
                            7);
                }
                lastRunnerRect = runnerRect;
            }
            
        });
        DragginMouseListener dragListener = new DragginMouseListener();
        addMouseListener(dragListener);
        addMouseMoveListener(dragListener);
        //		addMouseWheelListener(new MouseWheelListener() {
        //
        //			public void mouseScrolled(MouseEvent e) {
        //				scrollable.scrollBy(e.count);
        //				redraw();
        //			}
        //
        //		});
        animationRunnable = new AnimationRunnable(true, 300);
        animationThread = new Thread(animationRunnable);
    }
    
    public void setRunnerSize(float whole, float visible) {
        this.whole = whole;
        this.visibleHeight = visible;
        if (position > whole - visible)
            position = 0;
        redraw();
    }
    
    public void setPosition(float position) {
        //		if (position < 0 || position > whole - visible)
        //			throw new IllegalArgumentException("position is out of bounds");
        if (position < 0)
            position = 0;
        if (position > whole - visibleHeight)
            position = whole - visibleHeight;
        this.position = position;
        redraw();
    }
    
    public float getPosition() {
        return position;
    }
    
    public float beginMargin() {
        return beginMargin;
    }
    
    public float endMargin() {
        return endMargin;
    }
    
    public void setBeginMargin(float beginMargin) {
        this.beginMargin = beginMargin;
    }
    
    public void setEndMargin(float endMargin) {
        this.endMargin = endMargin;
    }
    
    @Override
    public Point computeSize(int hint, int hint2, boolean changed) {
        Point computedSize = super.computeSize(hint, hint2, changed);
        if (vertical)
            return new Point(14, computedSize.y);
        else
            return new Point(computedSize.x, 14);
    }
    
    // animation stuff
    
    private class AnimationRunnable implements Runnable {
        
        @SynchronizedWithMonitorOfField("animationLock")
        private boolean show;
        
        private final float time;
        
        public AnimationRunnable(boolean show, float time) {
            synchronized (animationLock) {
                this.show = show;
            }
            this.time = time;
        }
        
        public void run() {
            synchronized (animationLock) {
                runningAnimation = true;
            }
            
            long delay = 5;
            float step = delay / time;
            
            while (true) {
                synchronized (animationLock) {
                    if (((show && alpha >= 1) || (!show && alpha <= 0)) || !runningAnimation) {
                        runningAnimation = false;
                        break;
                    }
                    
                    Display.getDefault().syncExec(new Runnable() {
                        
                        public void run() {
                            if (!CoolScrollBar.this.isDisposed())
                                CoolScrollBar.this.redraw();
                        }
                        
                    });
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                    if (show)
                        alpha += step;
                    else
                        alpha -= step;
                }
            }
            
            synchronized (animationLock) {
                runningAnimation = false;
                if (show && alpha > 1)
                    alpha = 1;
                if (!show && alpha < 0)
                    alpha = 0;
            }
        }
        
    }
    
    private boolean show_ = false;
    
    private synchronized void animateAlpha(final boolean show) {
        show_ = show;
        
        new Thread(new Runnable() {
            
            public void run() {
                synchronized (animationLock) {
                    animationRunnable.show = show_;
                    
                    if (!runningAnimation) {
                        animationThread = new Thread(animationRunnable);
                        animationThread.start();
                    }
                }
            }
            
        }).start();
    }
    
    public void animateShow() {
        animateAlpha(true);
    }
    
    public void animateHide() {
        animateAlpha(false);
    }
    
    public boolean working() {
        return calculateRunnerRect() != null;
    }
    
}
