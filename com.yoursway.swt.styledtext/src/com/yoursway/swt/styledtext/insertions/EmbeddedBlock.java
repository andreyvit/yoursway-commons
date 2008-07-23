package com.yoursway.swt.styledtext.insertions;

import org.eclipse.swt.widgets.Composite;

import com.yoursway.utils.annotations.UseFromAnyThread;
import com.yoursway.utils.annotations.UseFromUIThread;

public interface EmbeddedBlock {
    
    @UseFromUIThread
    void init(Composite composite, YourSwayStyledTextEventSource la);
    
    @UseFromUIThread
    void redraw();
    
    @UseFromUIThread
    void dispose();
    
    @UseFromAnyThread
    boolean isDisposed();
    
}
