package com.yoursway.swt.styledtext.extended;

import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.custom.YourSwayStyledTextInternal;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

import com.yoursway.utils.annotations.UseFromAnyThread;
import com.yoursway.utils.annotations.UseFromUIThread;

public class ExtendedStyledText {
    
    YourSwayStyledTextInternal internal; //? rename to ExtendedTextWidget widget
    
    public ExtendedStyledText(Composite parent, int style) {
        try {
            internal = new YourSwayStyledTextInternal(parent, style);
        } catch (Throwable e) {
            e.getClass();
        }
    }
    
    @UseFromUIThread
    public void addExtendedModifyListener(final ExtendedModifyListener listener) {
        //! several listeners
        
        internal.addExtendedModifyListener(new ExtendedModifyListener() {
            public void modifyText(ExtendedModifyEvent e) {
                Widget _widget = e.widget;
                int _length = e.length;
                String _replacedText = e.replacedText;
                int _start = e.start;
                
                e.widget = null;
                e.length = textWithoutInsertions(_start, _length).length();
                e.replacedText = replacedTextWithoutInsertions(_replacedText, _start, _length);
                e.start = externalOffset(_start);
                
                if (e.length != 0 || e.replacedText.length() != 0) {
                    listener.modifyText(e);
                }
                
                //? check e fields
                
                e.widget = _widget;
                e.length = _length;
                e.replacedText = _replacedText;
                e.start = _start;
            }
            
            private String textWithoutInsertions(int _start, int _length) {
                if (_length == 0)
                    return "";
                int end = _start + _length - 1;
                if (insertionAtEdge(_start, _length))
                    end--;
                return withoutInsertions(internal.getText(_start, end));
            }
            
            private String replacedTextWithoutInsertions(String _replacedText, int _start, int _length) {
                String text = _replacedText;
                if (insertionAtEdge(_start, _length))
                    text = text.substring(0, text.length() - 1);
                return withoutInsertions(text);
            }
            
            private boolean insertionAtEdge(int _start, int _length) {
                int i = _start + _length;
                return (internal.getCharCount() > i)
                        && (internal.getText(i, i).equals(internal.insertionPlaceholder()));
            }
            
        });
    }
    
    @UseFromUIThread
    public void addVerifyKeyListener(final VerifyKeyListener listener) {
        //! several listeners
        
        internal.addVerifyKeyListener(new VerifyKeyListener() {
            public void verifyKey(VerifyEvent e) {
                //? change fields
                
                listener.verifyKey(e);
                
                //? change fields
            }
        });
    }
    
    private int externalLineIndex(int internalLineIndex) {
        return internalLineIndex - insertionLinesAbove(internalLineIndex, false);
    }
    
    private int internalLineIndex(int externalLineIndex) {
        int internalLineIndex = externalLineIndex + insertionLinesAbove(externalLineIndex, true);
        if (internal.isInsertionLine(internalLineIndex))
            throw new AssertionError("External line can't be insertion line.");
        return internalLineIndex;
    }
    
    private int insertionLinesAbove(int lineIndex, boolean external) {
        int workingLineIndex = lineIndex;
        int count = 0;
        for (int i = 0; i <= workingLineIndex; i++) {
            if (internal.isInsertionLine(i)) {
                count++;
                if (external)
                    workingLineIndex++;
            }
        }
        return count;
    }
    
    private int internalOffset(int externalOffset) {
        //? ineffective //> by lines
        int workingOffset = externalOffset;
        String text = internal.getText();
        char p = internal.insertionPlaceholder().charAt(0);
        for (int i = 0; i <= workingOffset; i++) {
            if (text.length() > i && text.charAt(i) == p) {
                workingOffset += 2;
                //! magic 2 == ("\n" + insertionPlaceholder).length()
            }
        }
        return workingOffset;
    }
    
    private int externalOffset(int internalOffset) {
        //? ineffective
        int workingOffset = internalOffset;
        String text = internal.getText();
        char p = internal.insertionPlaceholder().charAt(0);
        if (text.length() > internalOffset && text.charAt(internalOffset) == p)
            workingOffset--;
        return withoutInsertions(text.substring(0, workingOffset)).length();
    }
    
    @UseFromUIThread
    public int caretLine() {
        return externalLineIndex(internal.caretLine());
    }
    
    @UseFromUIThread
    public String getLine(int lineIndex) {
        return internal.getLine(internalLineIndex(lineIndex));
    }
    
    @UseFromUIThread
    public Point selectedLines() {
        Point internalLines = internal.selectedLines();
        int x = externalLineIndex(internalLines.x);
        int y = externalLineIndex(internalLines.y);
        return new Point(x, y);
    }
    
    @UseFromUIThread
    public void setFont(Font font) {
        internal.setFont(font);
    }
    
    @UseFromUIThread
    public void addBlock(int lineIndex, EmbeddedBlock block) {
        internal.addEmbeddedBlock(internalLineIndex(lineIndex), block);
    }
    
    @UseFromUIThread
    public void append(String string) {
        internal.append(string);
    }
    
    @UseFromUIThread
    public EmbeddedBlock existingInsertion(int lineIndex) {
        return internal.existingInsertion(internalLineIndex(lineIndex));
    }
    
    @UseFromUIThread
    public int getCharCount() {
        return externalOffset(internal.getCharCount()); //! it works :) change name?
    }
    
    @UseFromUIThread
    public int getLineAtOffset(int offset) {
        return externalLineIndex(internal.getLineAtOffset(internalOffset(offset)));
    }
    
    @UseFromUIThread
    public boolean inLastLine() {
        return caretLine() == getLineCount() - 1;
    }
    
    @UseFromUIThread
    public boolean lineHasInsertion(int lineIndex) {
        return internal.lineHasInsertion(internalLineIndex(lineIndex));
    }
    
    @UseFromUIThread
    public boolean removeInsertion(int lineIndex) {
        return internal.removeInsertion(internalLineIndex(lineIndex));
    }
    
    @UseFromUIThread
    public String getText(int start, int end) {
        return internal.getText(internalOffset(start), internalOffset(end));
    }
    
    @UseFromUIThread
    public String getSelectionText() {
        return withoutInsertions(internal.getSelectionText());
    }
    
    @UseFromAnyThread
    private String withoutInsertions(String text) {
        return text.replace("\n" + internal.insertionPlaceholder(), "");
    }
    
    @UseFromUIThread
    public void setSelection(int start) {
        internal.setSelection(internalOffset(start));
    }
    
    @UseFromUIThread
    public int getLineCount() {
        return externalLineIndex(internal.getLineCount()); //! it works :) change name?
    }
    
    @UseFromUIThread
    public void lineDown() {
        internal.invokeAction(ST.LINE_DOWN);
        if (internal.inInsertionLine())
            internal.invokeAction(ST.LINE_DOWN);
    }
    
    @UseFromUIThread
    public Color getBackground() {
        return internal.getBackground();
    }
    
    @UseFromUIThread
    public Rectangle getClientArea() {
        return internal.getClientArea();
    }
    
    public Shell getShell() {
        return internal.getShell();
    }
    
    public Display getDisplay() {
        return internal.getDisplay();
    }
    
}
