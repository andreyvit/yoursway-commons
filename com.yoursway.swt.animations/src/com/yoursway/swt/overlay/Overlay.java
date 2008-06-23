/**
 * 
 */
package com.yoursway.swt.overlay;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

public interface Overlay {
    
    Rectangle getBounds();
    
    void drawOverlayBackgroundOnto(GC gc, Rectangle bounds);
    
    void renderOffscreenImage(Image image);
    
    void dispose();
    
}
