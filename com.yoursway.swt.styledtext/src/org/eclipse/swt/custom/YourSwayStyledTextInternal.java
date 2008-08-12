package org.eclipse.swt.custom;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import com.yoursway.swt.additions.YsSwtGeometry;
import com.yoursway.swt.styledtext.extended.Inset;
import com.yoursway.swt.styledtext.extended.InsetSite;
import com.yoursway.swt.styledtext.extended.ResizeListener;
import com.yoursway.swt.styledtext.extended.internal.InsetPlace;
import com.yoursway.utils.annotations.Reentrant_CallFromAnyThread;
import com.yoursway.utils.annotations.UseFromUIThread;

@UseFromUIThread
public class YourSwayStyledTextInternal extends StyledText {
    
    private final Collection<InsetPlace> insetPlaces = new LinkedList<InsetPlace>();
    
    private final Collection<ResizeListener> resizeListeners = new LinkedList<ResizeListener>();
    
    public YourSwayStyledTextInternal(Composite parent, int style) {
        super(parent, style);
        
        addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                int start = e.start;
                int replaceCharCount = e.end - e.start;
                int newCharCount = e.text.length();
                
                Iterator<InsetPlace> it = insetPlaces.iterator();
                while (it.hasNext()) {
                    InsetPlace insetPlace = it.next();
                    int offset = insetPlace.offset();
                    
                    if (start <= offset && offset < start + replaceCharCount) {
                        insetPlace.dispose();
                        it.remove();
                    } else if (offset >= start) {
                        offset += newCharCount - replaceCharCount;
                        insetPlace.offset(offset);
                    }
                }
            }
        });
        addPaintObjectListener(new PaintObjectListener() {
            public void paintObject(PaintObjectEvent e) {
                int offset = e.style.start;
                
                for (InsetPlace insetPlace : insetPlaces) {
                    if (offset == insetPlace.offset()) {
                        insetPlace.updateLocation();
                        break;
                    }
                }
            }
        });
        addControlListener(new ControlListener() {
            public void controlMoved(ControlEvent e) {
                // nothing
            }
            
            public void controlResized(ControlEvent e) {
                for (ResizeListener listener : resizeListeners) {
                    listener.resized(getSize());
                }
                redraw(); //?
            }
        });
        
        ExtendedTextController controller = new ExtendedTextController(this);
        addVerifyKeyListener(controller);
        addKeyListener(controller);
        addMouseListener(controller);
    }
    
    @Override
    public void scroll(int destX, int destY, int x, int y, int width, int height, boolean all) {
        super.scroll(destX, destY, x, y, width, height, false);
    }
    
    @Override
    boolean scrollVertical(int pixels, boolean adjustScrollBar) {
        boolean scrolled = super.scrollVertical(pixels, adjustScrollBar);
        
        for (InsetPlace insetPlace : insetPlaces) {
            Point loc = insetPlace.getLocation();
            insetPlace.setLocation(loc.x, loc.y - pixels);
        }
        
        return scrolled;
    }
    
    @Override
    boolean scrollHorizontal(int pixels, boolean adjustScrollBar) {
        boolean scrolled = super.scrollHorizontal(pixels, adjustScrollBar);
        
        for (InsetPlace insetPlace : insetPlaces) {
            Point loc = insetPlace.getLocation();
            insetPlace.setLocation(loc.x - pixels, loc.y);
        }
        
        return scrolled;
    }
    
    @Override
    void scrollText(int srcY, int destY) {
        super.scrollText(srcY, destY);
        
        for (InsetPlace insetPlace : insetPlaces) {
            Point loc = insetPlace.getLocation();
            if (loc.y >= srcY)
                insetPlace.setLocation(loc.x, loc.y + destY - srcY);
        }
    }
    
    @Reentrant_CallFromAnyThread
    public String insetPlaceholder() {
        return "\uFFFC";
    }
    
    @Reentrant_CallFromAnyThread
    private int insetPlaceholderLength() throws AssertionError {
        if (insetPlaceholder().length() != 1)
            throw new AssertionError("An inset placeholder must have 1 char length.");
        return 1;
    }
    
    public void addInset(int lineIndex, final Inset inset) {
        int offset = lineEndOffset(lineIndex);
        replaceTextRange(offset, 0, "\n" + insetPlaceholder());
        offset++; // "\n"
        final Composite composite = new Composite(this, SWT.NO_FOCUS | SWT.NO_BACKGROUND);
        
        final InsetPlace insetPlace = new InsetPlace(inset, offset, composite, this);
        insetPlaces.add(insetPlace);
        
        composite.addControlListener(new ControlListener() {
            public void controlMoved(ControlEvent e) {
                // nothing
            }
            
            public void controlResized(ControlEvent e) {
                updateMetrics(insetPlace.offset(), composite.getSize());
            }
        });
        
        inset.init(composite, new InsetSite() {
            public Color getBackground() {
                return YourSwayStyledTextInternal.this.getBackground();
            }
            
            public Point clientAreaSize() {
                return YsSwtGeometry.size(getClientArea());
            }
            
            public void addResizeListener(ResizeListener listener) {
                resizeListeners.add(listener);
            }
        });
    }
    
    public int lineEndOffset(int lineIndex) {
        int lineOffset = getOffsetAtLine(lineIndex);
        int lineLength = getLine(lineIndex).length();
        return lineOffset + lineLength;
    }
    
    public Inset existingInset(int lineIndex) {
        int offset = lineEndOffset(lineIndex) + 1;
        for (InsetPlace insetPlace : insetPlaces) {
            if (insetPlace.offset() == offset)
                return insetPlace.inset();
        }
        return null;
    }
    
    public boolean removeInset(int lineIndex) {
        if (!lineHasInset(lineIndex))
            return false;
        
        int s = insetPlaces.size();
        
        int offset = lineEndOffset(lineIndex);
        //! must be 2 == ("\n" + insertionPlaceholder()).length()
        replaceTextRange(offset, 2, "");
        
        if (insetPlaces.size() != s - 1)
            throw new AssertionError("Inset object hasn't been removed from collection.");
        
        return true;
    }
    
    boolean lineHasInset() {
        return lineHasInset(selectedLines().y);
    }
    
    public boolean lineHasInset(int lineIndex) {
        return isInsetLine(lineIndex + 1);
    }
    
    //!
    public boolean isInsetLine(int lineIndex) {
        if (getLineCount() <= lineIndex)
            return false;
        return (getLine(lineIndex).equals(insetPlaceholder()));
    }
    
    private void updateMetrics(int offset, Point size) {
        StyleRange style = new StyleRange();
        style.start = offset;
        style.length = insetPlaceholderLength();
        int width = size.x - (size.x > 20 ? 20 : 0); // hack
        style.metrics = new GlyphMetrics(size.y, 0, width);
        setStyleRange(style);
        
        if (getCaretOffset() == offset + 2) //! magic
            showSelection();
    }
    
    void selectInsetLineEnd() {
        if (!lineHasInset())
            throw new AssertionError("Selected line must have an inset.");
        
        int offset = lineEndOffset(selectedLines().y + 1);
        setSelection(offset);
    }
    
    //?
    public Point selectedLines() {
        Point sel = getSelection();
        int firstLine = getLineAtOffset(sel.x);
        int lastLine = getLineAtOffset(sel.y);
        if (firstLine > lastLine)
            throw new AssertionError("First line of selection must be <= than last.");
        return new Point(firstLine, lastLine);
    }
    
    public boolean inInsetLine() {
        return isInsetLine(caretLine());
    }
    
    void moveCaretFromInsetLine(boolean selection) {
        if (inInsetLine())
            moveCaret(selection, atLineBegin() ? -1 : inLastLine() ? -2 : 1);
    }
    
    //?
    public int caretLine() {
        return getLineAtOffset(getCaretOffset());
    }
    
    private void moveCaret(boolean selection, int where) {
        if (selection) {
            Point sel = getSelection();
            if (caretAtSelectionEnd())
                setSelection(sel.x, sel.y + where);
            else
                setSelection(sel.x + where, sel.y);
        } else {
            setCaretOffset(getCaretOffset() + where);
        }
    }
    
    private boolean caretAtSelectionEnd() {
        return getCaretOffset() == getSelection().y;
    }
    
    boolean atLineBegin() {
        int offset = getOffsetAtLine(caretLine());
        return getCaretOffset() == offset;
    }
    
    boolean inLastLine() {
        return caretLine() == getLineCount() - 1;
    }
    
}
