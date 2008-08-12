package com.yoursway.swt.styledtext.extended;

import org.eclipse.swt.widgets.Composite;

import com.yoursway.utils.annotations.CallFromAnyThread_NonReentrant;
import com.yoursway.utils.annotations.UseFromUIThread;

@UseFromUIThread
public interface Inset {
    
    void init(Composite composite, InsetSite site);
    
    void redraw();
    
    void dispose();
    
    @CallFromAnyThread_NonReentrant
    boolean isDisposed();
    
}
