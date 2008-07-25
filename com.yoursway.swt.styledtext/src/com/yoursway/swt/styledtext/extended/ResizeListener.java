package com.yoursway.swt.styledtext.extended;

import org.eclipse.swt.graphics.Point;

import com.yoursway.utils.annotations.UseFromUIThread;

public interface ResizeListener {
    
    @UseFromUIThread
    void resized(Point size);
    
}
