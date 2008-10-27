package com.yoursway.swt.coolsidebar;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.yoursway.swt.coolsidebar.viewmodel.SidebarSection;

class SectionTitleView implements SidebarViewChild {
    
    public SectionTitleView(SidebarSection section, Composite parent) {
        final Canvas canvas = new Canvas(parent, SWT.TRANSPARENT);
        GridDataFactory.fillDefaults().grab(true, false).hint(0, 24).applyTo(canvas);
        
        final Font font = new Font(Display.getDefault(), "Gill Sans", 14, SWT.NONE);
        final String text = section.name();
        
        final Color textColor = new Color(canvas.getDisplay(), 96, 110, 128);
        final Color edgeColor = new Color(canvas.getDisplay(), 255, 255, 255);
        
        canvas.addPaintListener(new PaintListener() {
            
            public void paintControl(PaintEvent e) {
                e.gc.setFont(font);
                
                int width = canvas.getSize().x;
                Point textExtent = e.gc.textExtent(text);
                
                String resultText = text.toUpperCase();
                while (textExtent.x > width && resultText.length() > 0) {
                    resultText = text.substring(0, resultText.length() - 1);
                    textExtent = e.gc.textExtent(resultText + "...");
                }
                
                e.gc.setForeground(edgeColor);
                e.gc.setAlpha(100);
                e.gc.drawText(resultText, 10, 6, true);
                
                e.gc.setForeground(textColor);
                e.gc.setAlpha(255);
                e.gc.drawText(resultText, 10, 5, true);
            }
            
        });
        
    }
}
