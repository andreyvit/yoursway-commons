/**
 * 
 */
package com.yoursway.swt.overlay;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

public interface OverlayFactory {
    
    /**
     * Creates an overlay that allows drawing on top of the given control, on
     * top of its children, and (if the given <code>bounds</code> extend outside
     * of the control) its parents and their children.
     * 
     * @param bounds
     *      the bounds of area to allow drawing on, in the given control's
     *      coordinates
     */
    Overlay createOverlay(Control control, Rectangle bounds);
    
}
