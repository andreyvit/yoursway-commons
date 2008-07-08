package com.yoursway.swt.additions;

import org.eclipse.swt.graphics.Rectangle;

public class YsSwtGeometry {
    
    public static boolean isSameSize(Rectangle a, Rectangle b) {
        return a.width == b.width && a.height == b.height;
    }
    
    public static Rectangle duplicate(Rectangle r) {
        return new Rectangle(r.x, r.y, r.width, r.height);
    }
    
    public static Rectangle emptyRectangle() {
        return new Rectangle(0, 0, 0, 0);
    }
    
    public static Rectangle centeredRectange(Rectangle area, int width, int height) {
        return new Rectangle(area.x + (area.width - width) / 2, area.y + (area.height - height) / 2, width,
                height);
    }
    
    public static void set(Rectangle destination, Rectangle source) {
        destination.x = source.x;
        destination.y = source.y;
        destination.width = source.width;
        destination.height = source.height;
    }
    
    /**
     * It's safe to pass <code>source</code> as <code>piece</code> or <code>remainder</code>.
     */
    public static void divideIntoVerticalParts(Rectangle source, double ratio, Rectangle piece, Rectangle remainder) {
        piece.y = remainder.y = source.y;
        piece.height = remainder.height = source.height;
        
        int sourceWidth = source.width;
        int pieceWidth = (int) (sourceWidth * ratio + 0.5);
        piece.x = source.x;
        piece.width = pieceWidth;
        remainder.x = piece.x + pieceWidth;
        remainder.width = sourceWidth - pieceWidth;
    }
    
    /**
     * It's safe to pass <code>source</code> as <code>piece</code> or <code>remainder</code>.
     */
    public static void divideIntoHorizontalParts(Rectangle source, double ratio, Rectangle piece, Rectangle remainder) {
        piece.x = remainder.x = source.x;
        piece.width = remainder.width = source.width;
        
        int sourceHeight = source.height;
        int pieceHeight = (int) (sourceHeight * ratio + 0.5);
        piece.y = source.y;
        piece.height = pieceHeight;
        remainder.y = piece.y + pieceHeight;
        remainder.height = sourceHeight - pieceHeight;
    }
    
}
