package com.yoursway.swt.animations;

import com.yoursway.utils.annotations.UseFromAnyThread;

@UseFromAnyThread
public interface SizeAndAlphaAnimationApplier {
    
    void updateSize(int width, int height);
    
    void updateAlpha(int alpha);
    
    boolean visible();
    
}
