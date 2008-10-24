package com.yoursway.swt.scrollbar;

import static com.yoursway.swt.scrollbar.CompositeUtils.addAllChildrenListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class CoolScrollBarStyledTextBinding {
    
    private final StyledText styledText;
    private final CoolScrollBar vScrollBar;
    private final Composite composite;
    
    private int widgetHeight;
    private int textHeight;
    private int topPixel;
    
    public CoolScrollBarStyledTextBinding(StyledText styledText, CoolScrollBar scrollBar, Composite composite) {
        this.styledText = styledText;
        this.vScrollBar = scrollBar;
        this.composite = composite;
        
        installListener();
    }
    
    private void installListener() {
        styledText.addMouseWheelListener(new MouseWheelListener() {
            
            public void mouseScrolled(MouseEvent e) {
                vScroll(e.count, true);
            }
            
        });
        Listener updateListener = new Listener() {
            
            public void handleEvent(Event event) {
                updateScrollbarPosition();
            }
            
        };
        styledText.addListener(SWT.Modify, updateListener);
        styledText.addListener(SWT.Traverse, updateListener);
        styledText.addListener(SWT.Resize, updateListener);
        styledText.addListener(SWT.KeyDown, updateListener);
        new MouseEnterExitTracker(composite, new Listener() {
            
            public void handleEvent(Event event) {
                if (event.type == SWT.MouseEnter) {
                    vScrollBar.animateShow();
                } else if (event.type == SWT.MouseExit) {
                    vScrollBar.animateHide();
                }
                
            }
            
        });
        addAllChildrenListener(composite, SWT.MouseMove, new Listener() {
            
            public void handleEvent(Event event) {
                vScrollBar.animateShow();
            }
            
        });
        vScrollBar.addListener(SWT.Selection, new Listener() {
            
            public void handleEvent(Event event) {
                topPixel = (int) vScrollBar.getPosition();
                styledText.setTopPixel(topPixel);
            }
            
        });
    }
    
    public void vScroll(int count, boolean notify) {
        topPixel -= 5 * count;
        if (topPixel < 0)
            topPixel = 0;
        if (topPixel > textHeight - widgetHeight)
            topPixel = textHeight - widgetHeight;
        styledText.setTopPixel(topPixel);
        vScrollBar.setPosition(topPixel);
    }
    
    public void updateScrollbarPosition() {
        widgetHeight = styledText.getBounds().height;
        //textHeight = styledText.computeSize(styledText.getSize().x - 1, SWT.DEFAULT).y;
        int textLength = styledText.getText().length();
        if (textLength == 0) {
            textLength = 0;
        } else {
            Rectangle textBounds = styledText.getTextBounds(textLength - 1, textLength - 1);
            textHeight = styledText.getTopPixel() + textBounds.y + textBounds.height;
        }
        topPixel = styledText.getTopPixel();
        vScrollBar.setRunnerSize(textHeight, widgetHeight);
        vScrollBar.setPosition(topPixel);
    }
    
}
