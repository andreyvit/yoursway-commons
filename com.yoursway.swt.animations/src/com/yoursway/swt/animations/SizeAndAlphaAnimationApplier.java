package com.yoursway.swt.animations;

public interface SizeAndAlphaAnimationApplier {
    
    void updateSize(int width, int height);
    
    void updateAlpha(int alpha);
    
    boolean visible();
    
}
