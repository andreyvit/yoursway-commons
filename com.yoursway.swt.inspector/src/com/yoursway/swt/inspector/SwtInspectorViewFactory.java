package com.yoursway.swt.inspector;

public interface SwtInspectorViewFactory {
    
    SwtInspectorView createView(SwtInspectorViewCallback callback, SwtInspectorModel model);
    
}
