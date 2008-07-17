package com.yoursway.swt.inspector;


import static com.google.common.base.Join.join;
import static com.google.common.collect.Lists.newArrayList;
import static com.yoursway.utils.DebugOutputHelper.simpleNameOf;
import static java.lang.String.format;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

public class SwtInspectorController implements SwtInspectorViewCallback {
    
    private final SwtInspectorView view;
    private final Display display;
    private final SwtInspectorModel model;
    
    static final int KEYED_DATA       = 1 << 2;
    
    private static final int MASK = SWT.ALT | SWT.SHIFT;
    
    private final Listener controlHighlightListener = new Listener() {
        
        public void handleEvent(Event event) {
            switch (event.type) {
            case SWT.MouseMove:
                if ((event.stateMask & MASK) == MASK) {
                    turnOffTracking();
                    event.doit = false;
                    return;
                } 
                highlightActiveControl(event.widget);
                break;
            case SWT.MouseExit:
                if (event.widget instanceof Control)
                    controlHightlightView.removeHightlight((Control) event.widget);
                break;
            case SWT.MouseDown:
                if ((event.stateMask & MASK) == MASK && event.button == 1) {
                    turnOffTracking();
                    event.doit = false;
                }
                break;
            }
        }
        
    };
    
    private ControlHightlightView controlHightlightView;
    private final ControlRegistry controlRegistry;
    
    public SwtInspectorController(Display display, SwtInspectorViewFactory factory,
            ControlRegistry controlRegistry) {
        if (display == null)
            throw new NullPointerException("display is null");
        if (controlRegistry == null)
            throw new NullPointerException("controlRegistry is null");
        this.display = display;
        this.controlRegistry = controlRegistry;
        
        model = new SwtInspectorModel();
        model.highlightControls = true;
        
        controlHightlightView = new ControlHightlightView(display);
        
        view = factory.createView(this, model);
        view.setMessageToWelcome();
        
        turnOnTracking();
    }

    void highlightActiveControl(Widget widget) {
        if (!model.highlightControls)
            return;
        if (!(widget instanceof Control))
            return;
        Control control = (Control) widget;
        controlHightlightView.highlight(control);
        StringBuilder builder = new StringBuilder();
        
        int depth = 0;
        while (control != null) {
            if (depth > 0)
                builder.append("\n--------------------------------------------\n");
            describeControl(control, depth, builder);
            control = control.getParent();
            depth += 1;
        }
        view.setMessage(builder.toString());
    }
    
    private void describeControl(Control control, int depth, StringBuilder result) {
        ControlDescriptor descriptor = controlRegistry.lookup(control.getClass());
        appendName(control, depth, result, descriptor);
        appendStyles(control, result, descriptor);
        appendNewLine(result);
        if (control instanceof Composite) {
            appendLayout(result, ((Composite) control).getLayout());
        }
        appendLayoutData(control, result);
        appendNewLine(result);
        result.append(format("Size: %dx%d\n", control.getSize().x, control.getSize().y));
        appendUserData(control, result);
    }

    private void appendUserData(Control control, StringBuilder result) {
        String objectDataDescription;
        try {
            Field dataField = Widget.class.getDeclaredField("data");
            Field stateField = Widget.class.getDeclaredField("state");
            dataField.setAccessible(true);
            stateField.setAccessible(true);
            Object data = dataField.get(control);
            int state = (Integer) stateField.get(control);
            
            List<String> dataItems = newArrayList();
            if ((state & KEYED_DATA) == KEYED_DATA) {
                Object[] pairs = (Object[]) data;
                for (int i = 1; i < pairs.length; i += 2)
                    dataItems.add(format("%s (%s)", pairs[i], simpleNameOf(pairs[i + 1])));
                data = pairs[0];
            }
            if (data != null)
                dataItems.add(0, format("unnamed (%s)", simpleNameOf(data)));
            objectDataDescription = join(", ", dataItems);
        } catch (Throwable e) {
            e.printStackTrace();
            objectDataDescription = "**failed to obtain key/value data**";
            Object data = control.getData();
            if (data != null)
                objectDataDescription = format("unnamed (%s) ", simpleNameOf(data)) + objectDataDescription;
        }
        if (objectDataDescription.length() != 0) {
            appendNewLine(result);
            result.append(format("User data keys: %s", objectDataDescription));
        }
    }

    private void appendLayoutData(Control control, StringBuilder result) {
        result.append(format("Layout data: %s\n", control.getLayoutData()));
    }

    private void appendLayout(StringBuilder result, Layout layout) {
        result.append(format("Layout: %s\n", (layout == null ? "(none)" : layout.getClass()
                .getSimpleName())));
        if (layout != null)
            result.append(format("Layout.toString: %s\n", layout));
    }

    private void appendNewLine(StringBuilder result) {
        result.append("\n");
    }

    private void appendName(Control control, int depth, StringBuilder result, ControlDescriptor descriptor) {
        String controlDesignator = depth == 0 ? "Control" : "Parent " + depth;
        result.append(format("%s: %s\n", controlDesignator, descriptor.name(control)));
    }
    
    private void appendStyles(Control control, StringBuilder result, ControlDescriptor descriptor) {
        appendStyles(control, descriptor, "Specific styles", true, result);
        for (ControlDescriptor d = descriptor.baseDescriptor(); d != null; d = d.baseDescriptor())
            appendStyles(control, d, d.className() + " styles", false, result);
    }
    
    private void appendStyles(Control control, ControlDescriptor descriptor, String message,
            boolean force, StringBuilder result) {
        String styles = join(", ", descriptor.stylesOf(control));
        if (styles.length() == 0 && !force)
            return;
        result.append(format("%s: %s\n", message, (styles.length() == 0 ? "(none)" : styles)));
    }

    public void show() {
        view.open();
    }
    
    public void viewClosed() {
        turnOffTracking();
    }
    
    public void setHighlightControls(boolean enable) {
        if (enable == model.highlightControls)
            return;
        if (enable)
            turnOnTracking();
        else {
            turnOffTracking();
            view.setMessageToWelcome();
        }
    }

    private void turnOnTracking() {
        model.highlightControls = true;
        
        display.addFilter(SWT.MouseMove, controlHighlightListener);
        display.addFilter(SWT.MouseExit, controlHighlightListener);
        display.addFilter(SWT.MouseDown, controlHighlightListener);
        
        view.update();
    }
    
    protected void turnOffTracking() {
        model.highlightControls = false;
        
        display.removeFilter(SWT.MouseMove, controlHighlightListener);
        display.removeFilter(SWT.MouseExit, controlHighlightListener);
        display.removeFilter(SWT.MouseDown, controlHighlightListener);
        
        controlHightlightView.dehighlight();
        view.update();
    }
    
}
