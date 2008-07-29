package com.yoursway.swt.animations;

import com.yoursway.utils.annotations.CallFromAnyThread_NonReentrant;

@CallFromAnyThread_NonReentrant
public interface SizeAndAlphaAnimationApplier {
    
    void updateSize(int width, int height);
    
    void updateAlpha(int alpha);
    
    boolean visible();
    
}
