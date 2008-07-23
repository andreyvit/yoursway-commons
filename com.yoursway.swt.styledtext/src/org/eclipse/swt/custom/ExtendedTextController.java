package org.eclipse.swt.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.VerifyEvent;


public class ExtendedTextController implements VerifyKeyListener, KeyListener, MouseListener {
    
    private final YourSwayStyledTextInternal view;
    
    public ExtendedTextController(YourSwayStyledTextInternal view) {
        this.view = view;
        
    }
    
    public void verifyKey(VerifyEvent e) {
        
        if (e.character == '\n' || e.character == '\r') {
            if (view.lineHasInsertion()) {
                if (atLineEnd())
                    view.selectInsertionLineEnd();
                //? else view.removeInsertion(view.caretLine());
            }
        }

        else if (e.keyCode == SWT.DEL) {
            if (view.lineHasInsertion() && atLineEnd())
                view.removeInsertion(view.caretLine());
        }

        else if (e.character == '\b') {
            if (view.atLineBegin() && !lineEmpty())
                removePrevLineInserionIfExists();
        }

        else {
            // not handle or block other keys
        }
    }
    
    public void keyPressed(KeyEvent e) {
        if (view.inInsertionLine()) {
            boolean selection = ((e.stateMask & SWT.SHIFT) == SWT.SHIFT);
            
            if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_LEFT || e.character == '\b') {
                view.invokeAction(selection ? ST.SELECT_LINE_UP : ST.LINE_UP);
            }

            else if (e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ARROW_RIGHT) {
                if (view.inLastLine()) {
                    view.invokeAction(selection ? ST.SELECT_LINE_UP : ST.LINE_UP);
                    if (e.keyCode == SWT.ARROW_RIGHT)
                        view.invokeAction(selection ? ST.SELECT_LINE_END : ST.LINE_END);
                } else
                    view.invokeAction(selection ? ST.SELECT_LINE_DOWN : ST.LINE_DOWN);
            }

            else {
                // not handle or block other keys
            }
        }
        
    }
    
    public void keyReleased(KeyEvent e) {
        // nothing          
    }
    
    public void mouseDoubleClick(MouseEvent e) {
        // nothing        
    }
    
    public void mouseDown(MouseEvent e) {
        view.moveCaretFromInsertionLine(false);
    }
    
    public void mouseUp(MouseEvent e) {
        view.moveCaretFromInsertionLine(true);
    }
    
    private boolean lineEmpty() {
        return view.getLine(view.caretLine()).length() == 0;
    }
    
    private void removePrevLineInserionIfExists() {
        int prevLine = view.caretLine() - 2;
        if (prevLine >= 0) {
            if (view.lineHasInsertion(prevLine))
                view.removeInsertion(prevLine);
        }
    }
    
    private boolean atLineEnd() {
        int offset = view.lineEndOffset(view.caretLine());
        return view.getCaretOffset() == offset;
    }
    
}
