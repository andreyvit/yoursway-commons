package com.yoursway.swt.additions;

import static com.yoursway.swt.additions.YsSwtUtils.currentDisplay;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

public class YsStandardFonts {
    
    private static Font smallFont = null;

    public static Font createSmallFont(Display display) {
        FontData data = display.getSystemFont().getFontData()[0];
        data.setHeight(9);
        return new Font(display, data);
    }
    
    public static Font smallFont() {
        if (smallFont == null)
            smallFont = createSmallFont(currentDisplay());
        return smallFont;
    }

}
