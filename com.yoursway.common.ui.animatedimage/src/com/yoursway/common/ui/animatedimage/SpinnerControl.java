package com.yoursway.common.ui.animatedimage;

import java.io.InputStream;

import org.eclipse.swt.widgets.Composite;

public class SpinnerControl extends AnimatedGifControl {
    
    public SpinnerControl(Composite parent, int style) {
        super(parent, style);
        loadImage(getIndicatorGifAsStream());
    }
    
    private static InputStream getIndicatorGifAsStream() throws AssertionError {
        InputStream resource = SpinnerControl.class.getResourceAsStream("indicator.gif");
        if (resource == null)
            throw new AssertionError("indicator.gif not found");
        return resource;
    }
    
}
