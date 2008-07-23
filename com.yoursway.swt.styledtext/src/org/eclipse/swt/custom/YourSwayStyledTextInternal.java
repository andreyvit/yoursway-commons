package org.eclipse.swt.custom;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import com.yoursway.swt.styledtext.extended.EmbeddedBlock;
import com.yoursway.swt.styledtext.extended.ResizeListener;
import com.yoursway.swt.styledtext.extended.YourSwayStyledTextEventSource;
import com.yoursway.swt.styledtext.extended.internal.EmbeddedBlockSite;
import com.yoursway.utils.annotations.UseFromAnyThread;
import com.yoursway.utils.annotations.UseFromUIThread;

public class YourSwayStyledTextInternal extends StyledText {
    
    private final Collection<EmbeddedBlockSite> blockSites = new LinkedList<EmbeddedBlockSite>();
    
    private final Collection<ResizeListener> resizeListeners = new LinkedList<ResizeListener>();
    
    public YourSwayStyledTextInternal(Composite parent, int style) {
        super(parent, style);
        
        addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                int start = e.start;
                int replaceCharCount = e.end - e.start;
                int newCharCount = e.text.length();
                
                Iterator<EmbeddedBlockSite> it = blockSites.iterator();
                while (it.hasNext()) {
                    EmbeddedBlockSite insertion = it.next();
                    int offset = insertion.offset();
                    
                    if (start <= offset && offset < start + replaceCharCount) {
                        insertion.dispose();
                        it.remove();
                    } else if (offset >= start) {
                        offset += newCharCount - replaceCharCount;
                        insertion.offset(offset);
                    }
                }
            }
        });
        addPaintObjectListener(new PaintObjectListener() {
            public void paintObject(PaintObjectEvent e) {
                int offset = e.style.start;
                
                for (EmbeddedBlockSite insertion : blockSites) {
                    if (offset == insertion.offset()) {
                        insertion.updateLocation();
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
    @UseFromUIThread
    public void scroll(int destX, int destY, int x, int y, int width, int height, boolean all) {
        super.scroll(destX, destY, x, y, width, height, false);
    }
    
    @Override
    boolean scrollVertical(int pixels, boolean adjustScrollBar) {
        boolean scrolled = super.scrollVertical(pixels, adjustScrollBar);
        
        for (EmbeddedBlockSite insertion : blockSites) {
            Point loc = insertion.getLocation();
            insertion.setLocation(loc.x, loc.y - pixels);
        }
        
        return scrolled;
    }
    
    @Override
    boolean scrollHorizontal(int pixels, boolean adjustScrollBar) {
        boolean scrolled = super.scrollHorizontal(pixels, adjustScrollBar);
        
        for (EmbeddedBlockSite insertion : blockSites) {
            Point loc = insertion.getLocation();
            insertion.setLocation(loc.x - pixels, loc.y);
        }
        
        return scrolled;
    }
    
    @Override
    void scrollText(int srcY, int destY) {
        super.scrollText(srcY, destY);
        
        for (EmbeddedBlockSite insertion : blockSites) {
            Point loc = insertion.getLocation();
            if (loc.y >= srcY)
                insertion.setLocation(loc.x, loc.y + destY - srcY);
        }
    }
    
    @UseFromAnyThread
    public String insertionPlaceholder() {
        return "\uFFFC";
    }
    
    @UseFromAnyThread
    private int insertionPlaceholderLength() throws AssertionError {
        if (insertionPlaceholder().length() != 1)
            throw new AssertionError("An insertion placeholder must have 1 char length.");
        return 1;
    }
    
    @UseFromUIThread
    public void addEmbeddedBlock(int lineIndex, final EmbeddedBlock block) {
        int offset = lineEndOffset(lineIndex);
        replaceTextRange(offset, 0, "\n" + insertionPlaceholder());
        offset++; // "\n"
        final Composite composite = new Composite(this, SWT.NO_FOCUS | SWT.NO_BACKGROUND);
        
        final EmbeddedBlockSite site = new EmbeddedBlockSite(block, offset, composite, this);
        blockSites.add(site);
        
        composite.addControlListener(new ControlListener() {
            public void controlMoved(ControlEvent e) {
                // nothing
            }
            
            public void controlResized(ControlEvent e) {
                updateMetrics(site.offset(), composite.getSize());
            }
        });
        
        block.init(composite, new YourSwayStyledTextEventSource() {
            public void addResizeListener(ResizeListener listener) {
                resizeListeners.add(listener);
            }
        });
    }
    
    @UseFromUIThread
    public int lineEndOffset(int lineIndex) {
        int lineOffset = getOffsetAtLine(lineIndex);
        int lineLength = getLine(lineIndex).length();
        return lineOffset + lineLength;
    }
    
    @UseFromUIThread
    public EmbeddedBlock existingInsertion(int lineIndex) {
        int offset = lineEndOffset(lineIndex) + 1;
        for (EmbeddedBlockSite insertion : blockSites) {
            if (insertion.offset() == offset)
                return insertion.content();
        }
        return null;
    }
    
    @UseFromUIThread
    public boolean removeInsertion(int lineIndex) {
        if (!lineHasInsertion(lineIndex))
            return false;
        
        int s = blockSites.size();
        
        int offset = lineEndOffset(lineIndex);
        //! must be 2 == ("\n" + insertionPlaceholder()).length()
        replaceTextRange(offset, 2, "");
        
        if (blockSites.size() != s - 1)
            throw new AssertionError("Insertion object hasn't been removed from collection.");
        
        return true;
    }
    
    @UseFromUIThread
    boolean lineHasInsertion() {
        return lineHasInsertion(selectedLines().y);
    }
    
    @UseFromUIThread
    public boolean lineHasInsertion(int lineIndex) {
        return isInsertionLine(lineIndex + 1);
    }
    
    //!
    @UseFromUIThread
    public boolean isInsertionLine(int lineIndex) {
        if (getLineCount() <= lineIndex)
            return false;
        return (getLine(lineIndex).equals(insertionPlaceholder()));
    }
    
    @UseFromUIThread
    private void updateMetrics(int offset, Point size) {
        StyleRange style = new StyleRange();
        style.start = offset;
        style.length = insertionPlaceholderLength();
        int width = size.x - (size.x > 20 ? 20 : 0); // hack
        style.metrics = new GlyphMetrics(size.y, 0, width);
        setStyleRange(style);
        
        showSelection(); //? when
    }
    
    @UseFromUIThread
    void selectInsertionLineEnd() {
        if (!lineHasInsertion())
            throw new AssertionError("Selected line must have an insertion.");
        
        int offset = lineEndOffset(selectedLines().y + 1);
        setSelection(offset);
    }
    
    //?
    @UseFromUIThread
    public Point selectedLines() {
        Point sel = getSelection();
        int firstLine = getLineAtOffset(sel.x);
        int lastLine = getLineAtOffset(sel.y);
        if (firstLine > lastLine)
            throw new AssertionError("First line of selection must be <= than last.");
        return new Point(firstLine, lastLine);
    }
    
    @UseFromUIThread
    public boolean inInsertionLine() {
        return isInsertionLine(caretLine());
    }
    
    @UseFromUIThread
    public void moveCaretFromInsertionLine(boolean selection) {
        if (inInsertionLine())
            moveCaret(selection, atLineBegin() ? -1 : inLastLine() ? -2 : 1);
    }
    
    //?
    @UseFromUIThread
    public int caretLine() {
        return getLineAtOffset(getCaretOffset());
    }
    
    @UseFromUIThread
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
    
    @UseFromUIThread
    private boolean caretAtSelectionEnd() {
        return getCaretOffset() == getSelection().y;
    }
    
    @UseFromUIThread
    boolean atLineBegin() {
        int offset = getOffsetAtLine(caretLine());
        return getCaretOffset() == offset;
    }
    
    @UseFromUIThread
    boolean inLastLine() {
        return caretLine() == getLineCount() - 1;
    }
    
}
