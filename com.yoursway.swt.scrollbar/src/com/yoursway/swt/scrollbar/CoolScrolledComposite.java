/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.yoursway.swt.scrollbar;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;

public class CoolScrolledComposite extends Composite {
    
    Control content;
    Listener contentListener;
    Listener filter;
    
    int minHeight = 0;
    int minWidth = 0;
    boolean expandHorizontal = false;
    boolean expandVertical = false;
    
    boolean showFocusedControl = false;
    
    CoolScrollBar horizontalBar;
    CoolScrollBar verticalBar;
    
    Composite composite;
    
    public Composite parentComposite() {
        return composite;
    }
    
    @Override
    public ScrollBar getHorizontalBar() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ScrollBar getVerticalBar() {
        throw new UnsupportedOperationException();
    }
    
    public CoolScrolledComposite(Composite parent, int style) {
        super(parent, SWT.NONE);
        
        composite = new Composite(this, style);
        composite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL)
                .create());
        
        composite.setLayout(new CoolScrolledCompositeLayout());
        
        verticalBar = new CoolScrollBar(this, SWT.TRANSPARENT, true);
        verticalBar.setLayoutData(GridDataFactory.fillDefaults().grab(false, true).create());
        
        horizontalBar = new CoolScrollBar(this, SWT.TRANSPARENT, false);
        horizontalBar.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
        
        verticalBar.animateShow();
        horizontalBar.animateShow();
        
        super.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).spacing(1, 1).create());
        
        super.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        
        this.addMouseWheelListener(new MouseWheelListener() {
            
            public void mouseScrolled(MouseEvent e) {
                float position = verticalBar.getPosition();
                position -= e.count;
                verticalBar.setPosition(position);
                vScroll();
            }
            
        });
        
        // hBar.setVisible(false);
        horizontalBar.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                hScroll();
            }
        });
        
        // vBar.setVisible(false);
        verticalBar.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                vScroll();
            }
        });
        
        contentListener = new Listener() {
            public void handleEvent(Event e) {
                if (e.type != SWT.Resize)
                    return;
                layout(false);
            }
        };
        
        filter = new Listener() {
            public void handleEvent(Event event) {
                if (event.widget instanceof Control) {
                    Control control = (Control) event.widget;
                    if (contains(control))
                        showControl(control);
                }
            }
        };
        
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                getDisplay().removeFilter(SWT.FocusIn, filter);
            }
        });
    }
    
    boolean contains(Control control) {
        if (control == null || control.isDisposed())
            return false;
        
        Composite parent = control.getParent();
        while (parent != null && !(parent instanceof Shell)) {
            if (composite == parent)
                return true;
            parent = parent.getParent();
        }
        return false;
    }
    
    /**
     * Returns <code>true</code> if the content control will be expanded to fill
     * available horizontal space.
     * 
     * @return the receiver's horizontal expansion state
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     * 
     * @since 3.2
     */
    public boolean getExpandHorizontal() {
        checkWidget();
        return expandHorizontal;
    }
    
    /**
     * Returns <code>true</code> if the content control will be expanded to fill
     * available vertical space.
     * 
     * @return the receiver's vertical expansion state
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     * 
     * @since 3.2
     */
    public boolean getExpandVertical() {
        checkWidget();
        return expandVertical;
    }
    
    /**
     * Returns the minimum width of the content control.
     * 
     * @return the minimum width
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     * 
     * @since 3.2
     */
    public int getMinWidth() {
        checkWidget();
        return minWidth;
    }
    
    /**
     * Returns the minimum height of the content control.
     * 
     * @return the minimum height
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     * 
     * @since 3.2
     */
    public int getMinHeight() {
        checkWidget();
        return minHeight;
    }
    
    /**
     * Get the content that is being scrolled.
     * 
     * @return the control displayed in the content area
     */
    public Control getContent() {
        // checkWidget();
        return content;
    }
    
    /**
     * Returns <code>true</code> if the receiver automatically scrolls to a
     * focused child control to make it visible. Otherwise, returns
     * <code>false</code>.
     * 
     * @return a boolean indicating whether focused child controls are
     *         automatically scrolled into the viewport
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     * 
     * @since 3.4
     */
    public boolean getShowFocusedControl() {
        checkWidget();
        return showFocusedControl;
    }
    
    void hScroll() {
        if (content == null)
            return;
        Point location = content.getLocation();
        content.setLocation((int) -horizontalBar.getPosition(), location.y);
    }
    
    boolean needHScroll(Rectangle contentRect, boolean vVisible) {
        Rectangle hostRect = composite.getBounds();
        
        if (!expandHorizontal && contentRect.width > hostRect.width)
            return true;
        if (expandHorizontal && minWidth > hostRect.width)
            return true;
        return false;
    }
    
    boolean needVScroll(Rectangle contentRect, boolean hVisible) {
        Rectangle hostRect = getBounds();
        
        if (!expandVertical && contentRect.height > hostRect.height)
            return true;
        if (expandVertical && minHeight > hostRect.height)
            return true;
        return false;
    }
    
    /**
     * Return the point in the content that currently appears in the top left
     * corner of the scrolled composite.
     * 
     * @return the point in the content that currently appears in the top left
     *         corner of the scrolled composite. If no content has been set,
     *         this returns (0, 0).
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     * 
     * @since 2.0
     */
    public Point getOrigin() {
        checkWidget();
        if (content == null)
            return new Point(0, 0);
        Point location = content.getLocation();
        return new Point(-location.x, -location.y);
    }
    
    /**
     * Scrolls the content so that the specified point in the content is in the
     * top left corner. If no content has been set, nothing will occur.
     * 
     * Negative values will be ignored. Values greater than the maximum scroll
     * distance will result in scrolling to the end of the scrollbar.
     * 
     * @param origin
     *            the point on the content to appear in the top left corner
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                <li>ERROR_INVALID_ARGUMENT - value of origin is outside of
     *                content
     *                </ul>
     * @since 2.0
     */
    public void setOrigin(Point origin) {
        setOrigin(origin.x, origin.y);
    }
    
    /**
     * Scrolls the content so that the specified point in the content is in the
     * top left corner. If no content has been set, nothing will occur.
     * 
     * Negative values will be ignored. Values greater than the maximum scroll
     * distance will result in scrolling to the end of the scrollbar.
     * 
     * @param x
     *            the x coordinate of the content to appear in the top left
     *            corner
     * 
     * @param y
     *            the y coordinate of the content to appear in the top left
     *            corner
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     * 
     * @since 2.0
     */
    public void setOrigin(int x, int y) {
        checkWidget();
        if (content == null)
            return;
        
        horizontalBar.setPosition(-x);
        verticalBar.setPosition(-y);
        //		
        //		ScrollBar hBar = getHorizontalBar();
        //		if (hBar != null) {
        //			hBar.setSelection(x);
        //			x = -hBar.getSelection();
        //		} else {
        //			x = 0;
        //		}
        //		ScrollBar vBar = getVerticalBar();
        //		if (vBar != null) {
        //			vBar.setSelection(y);
        //			y = -vBar.getSelection();
        //		} else {
        //			y = 0;
        //		}
        content.setLocation(x, y);
    }
    
    /**
     * Set the content that will be scrolled.
     * 
     * @param content
     *            the control to be displayed in the content area
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public void setContent(Control content) {
        if (content == null)
            throw new NullPointerException("content is null");
        checkWidget();
        if (this.content != null && !this.content.isDisposed()) {
            this.content.removeListener(SWT.Resize, contentListener);
            this.content.setBounds(new Rectangle(-200, -200, 0, 0)); // ??? ;)
        }
        //		content.setParent(composite);
        this.content = content;
        //			if (vBar != null) {
        //				vBar.setMaximum(0);
        //				vBar.setThumb(0);
        //				vBar.setSelection(0);
        //			}
        //			if (hBar != null) {
        //				hBar.setMaximum(0);
        //				hBar.setThumb(0);
        //				hBar.setSelection(0);
        //			}
        content.setLocation(0, 0);
        layout(false);
        this.content.addListener(SWT.Resize, contentListener);
        
    }
    
    /**
     * Configure the ScrolledComposite to resize the content object to be as
     * wide as the ScrolledComposite when the width of the ScrolledComposite is
     * greater than the minimum width specified in setMinWidth. If the
     * ScrolledComposite is less than the minimum width, the content will not be
     * resized and instead the horizontal scroll bar will be used to view the
     * entire width. If expand is false, this behaviour is turned off. By
     * default, this behaviour is turned off.
     * 
     * @param expand
     *            true to expand the content control to fill available
     *            horizontal space
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public void setExpandHorizontal(boolean expand) {
        checkWidget();
        if (expand == expandHorizontal)
            return;
        expandHorizontal = expand;
        layout(false);
    }
    
    /**
     * Configure the ScrolledComposite to resize the content object to be as
     * tall as the ScrolledComposite when the height of the ScrolledComposite is
     * greater than the minimum height specified in setMinHeight. If the
     * ScrolledComposite is less than the minimum height, the content will not
     * be resized and instead the vertical scroll bar will be used to view the
     * entire height. If expand is false, this behaviour is turned off. By
     * default, this behaviour is turned off.
     * 
     * @param expand
     *            true to expand the content control to fill available vertical
     *            space
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public void setExpandVertical(boolean expand) {
        checkWidget();
        if (expand == expandVertical)
            return;
        expandVertical = expand;
        layout(false);
    }
    
    /**
     * Sets the layout which is associated with the receiver to be the argument
     * which may be null.
     * <p>
     * Note: No Layout can be set on this Control because it already manages the
     * size and position of its children.
     * </p>
     * 
     * @param layout
     *            the receiver's new layout or null
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    @Override
    public void setLayout(Layout layout) {
        checkWidget();
        return;
    }
    
    /**
     * Specify the minimum height at which the ScrolledComposite will begin
     * scrolling the content with the vertical scroll bar. This value is only
     * relevant if setExpandVertical(true) has been set.
     * 
     * @param height
     *            the minimum height or 0 for default height
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public void setMinHeight(int height) {
        setMinSize(minWidth, height);
    }
    
    /**
     * Specify the minimum width and height at which the ScrolledComposite will
     * begin scrolling the content with the horizontal scroll bar. This value is
     * only relevant if setExpandHorizontal(true) and setExpandVertical(true)
     * have been set.
     * 
     * @param size
     *            the minimum size or null for the default size
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public void setMinSize(Point size) {
        if (size == null) {
            setMinSize(0, 0);
        } else {
            setMinSize(size.x, size.y);
        }
    }
    
    /**
     * Specify the minimum width and height at which the ScrolledComposite will
     * begin scrolling the content with the horizontal scroll bar. This value is
     * only relevant if setExpandHorizontal(true) and setExpandVertical(true)
     * have been set.
     * 
     * @param width
     *            the minimum width or 0 for default width
     * @param height
     *            the minimum height or 0 for default height
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public void setMinSize(int width, int height) {
        checkWidget();
        if (width == minWidth && height == minHeight)
            return;
        minWidth = Math.max(0, width);
        minHeight = Math.max(0, height);
        layout(false);
    }
    
    /**
     * Specify the minimum width at which the ScrolledComposite will begin
     * scrolling the content with the horizontal scroll bar. This value is only
     * relevant if setExpandHorizontal(true) has been set.
     * 
     * @param width
     *            the minimum width or 0 for default width
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     */
    public void setMinWidth(int width) {
        setMinSize(width, minHeight);
    }
    
    /**
     * Configure the receiver to automatically scroll to a focused child control
     * to make it visible.
     * 
     * If show is <code>false</code>, show a focused control is off. By default,
     * show a focused control is off.
     * 
     * @param show
     *            <code>true</code> to show a focused control.
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     * 
     * @since 3.4
     */
    public void setShowFocusedControl(boolean show) {
        checkWidget();
        if (showFocusedControl == show)
            return;
        Display display = getDisplay();
        display.removeFilter(SWT.FocusIn, filter);
        showFocusedControl = show;
        if (!showFocusedControl)
            return;
        display.addFilter(SWT.FocusIn, filter);
        Control control = display.getFocusControl();
        if (contains(control))
            showControl(control);
    }
    
    /**
     * Scrolls the content of the receiver so that the control is visible.
     * 
     * @param control
     *            the control to be shown
     * 
     * @exception IllegalArgumentException
     *                <ul>
     *                <li>ERROR_NULL_ARGUMENT - if the control is null</li>
     *                <li>ERROR_INVALID_ARGUMENT - if the control has been
     *                disposed</li>
     *                </ul>
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     * 
     * @since 3.4
     */
    public void showControl(Control control) {
        checkWidget();
        if (control == null)
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        if (control.isDisposed())
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        if (!contains(control))
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        
        Rectangle itemRect = getDisplay().map(control.getParent(), composite, control.getBounds());
        Rectangle area = composite.getClientArea();
        Point origin = getOrigin();
        if (itemRect.x < 0)
            origin.x = Math.max(0, origin.x + itemRect.x);
        if (itemRect.y < 0)
            origin.y = Math.max(0, origin.y + itemRect.y);
        if (area.width < itemRect.x + itemRect.width)
            origin.x = Math.max(0, origin.x + itemRect.x + itemRect.width - area.width);
        if (area.height < itemRect.y + itemRect.height)
            origin.y = Math.max(0, origin.y + itemRect.y + itemRect.height - area.height);
        setOrigin(origin);
    }
    
    void vScroll() {
        if (content == null)
            return;
        
        Point location = content.getLocation();
        content.setLocation(location.x, (int) -verticalBar.getPosition());
    }
}
