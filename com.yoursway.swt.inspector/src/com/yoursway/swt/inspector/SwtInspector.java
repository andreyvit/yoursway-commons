package com.yoursway.swt.inspector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;

public class SwtInspector {
    
    private static final int MASK = SWT.ALT | SWT.SHIFT;
    
    private static Listener listener = new Listener() {
        
        public void handleEvent(Event event) {
            if ((event.stateMask & MASK) == MASK && event.keyCode == SWT.F2) {
                event.doit = false;
                showSwtInspector();
            }
        }
        
    };
    
    private static boolean listenerInstalled = false;
    
    private static void doHookSwtInspectorHotkey() {
        if (listenerInstalled)
            return;
        listenerInstalled = true;
        Display.getDefault().addFilter(SWT.KeyDown, listener);
    }
    
    public static void hookSwtInspectorHotkey() {
        Display.getDefault().asyncExec(new Runnable() {
            
            public void run() {
                doHookSwtInspectorHotkey();
            }
            
        });
    }
    
    public static void showSwtInspector() {
        hookSwtInspectorHotkey();
        
        final Display display = Display.getDefault();
        
        ControlRegistry cr = new ControlRegistry();
        cr.register(Control.class, "BORDER, LEFT_TO_RIGHT, RIGHT_TO_LEFT");
        cr.register(Label.class, "SEPARATOR, HORIZONTAL, VERTICAL, SHADOW_IN, SHADOW_OUT, SHADOW_NONE, "
                + "CENTER, LEFT, RIGHT, WRAP");
        cr.register(Scrollable.class, "H_SCROLL, V_SCROLL");
        cr.register(Composite.class, "NO_BACKGROUND, NO_FOCUS, NO_MERGE_PAINTS, NO_REDRAW_RESIZE, "
                + "NO_RADIO_GROUP, EMBEDDED, DOUBLE_BUFFERED");
        cr.register(Canvas.class, "");
        cr.register(Decorations.class, "BORDER, CLOSE, MIN, MAX, NO_TRIM, RESIZE, TITLE, ON_TOP, TOOL");
        cr.register(Shell.class, "APPLICATION_MODAL, MODELESS, PRIMARY_MODAL, SYSTEM_MODAL");
        cr.register(Button.class, "ARROW, CHECK, PUSH, RADIO, TOGGLE, FLAT, UP, DOWN, LEFT, RIGHT, CENTER");
        //        cr.register(Button.class, "");
        
        SwtInspectorController controller = new SwtInspectorController(display,
                new SwtInspectorViewFactory() {
                    public SwtInspectorView createView(SwtInspectorViewCallback callback,
                            SwtInspectorModel model) {
                        return new SwtInspectorView(display, callback, model);
                    }
                }, cr);
        controller.show();
    }
    
}
