/**
 * 
 */
package com.yoursway.swt.animations.flip;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Listens to a Flipper and modifies the top control of a StackLayout when a
 * flip occurs.
 */
public class StackLayoutFlipperListener implements FlipperListener {
    
    private final Flipper flipper;
    private final StackLayout layout;
    private final Composite composite;
    
    /**
     * @param flipper
     *      the flipper to listen to
     * @param composite
     *      the composite that has a <code>StackLayout</code>
     */
    public StackLayoutFlipperListener(Flipper flipper, Composite composite) {
        if (flipper == null)
            throw new NullPointerException("flipper is null");
        if (composite == null)
            throw new NullPointerException("composite is null");
        
        this.flipper = flipper;
        this.composite = composite;
        this.layout = (StackLayout) composite.getLayout();
        
        flipper.addListener(this);
        layout.topControl = flipper.getTopControl();
    }
    
    public void flipped() {
        layout.topControl = flipper.getTopControl();
        composite.layout();
    }
    
    public void flipperDisposed() {
    }
    
}
