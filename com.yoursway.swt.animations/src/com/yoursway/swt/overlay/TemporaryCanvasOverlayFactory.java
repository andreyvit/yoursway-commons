/**
 * 
 */
package com.yoursway.swt.overlay;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

public class TemporaryCanvasOverlayFactory implements OverlayFactory {
    
    public Overlay createOverlay(Control control, Rectangle bounds) {
        return new TemporaryCanvasOverlay(control, bounds);
    }

}
