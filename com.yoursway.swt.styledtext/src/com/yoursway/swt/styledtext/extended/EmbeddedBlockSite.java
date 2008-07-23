package com.yoursway.swt.styledtext.extended;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;

public interface EmbeddedBlockSite {
    
    Color getBackground();
    
    void addResizeListener(ResizeListener listener);
    
    Point clientAreaSize();
    
}
