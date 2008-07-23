package com.yoursway.swt.styledtext.extended;

import org.eclipse.swt.widgets.Composite;

import com.yoursway.utils.annotations.UseFromAnyThread;
import com.yoursway.utils.annotations.UseFromUIThread;

@UseFromUIThread
public interface EmbeddedBlock {
    
    void init(Composite composite, EmbeddedBlockSite site);
    
    void redraw();
    
    void dispose();
    
    @UseFromAnyThread
    boolean isDisposed();
    
}
