package com.yoursway.modelediting.swt;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.yoursway.modelediting.Fragment;
import com.yoursway.modelediting.IModelListener;
import com.yoursway.modelediting.Model;
import com.yoursway.modelediting.ReplaceImpossibleException;

public class ModelReconciler implements IModelListener {
    
    private class StyledTextListener implements Listener {
        
        boolean disabled = false;
        
        public void handleEvent(Event event) {
            if (disabled)
                return;
            if (event.type == SWT.Verify) {
                event.doit = false;
                try {
                    if (canModify(event.start, event.end))
                        modify(event.start, event.end, event.text);
                } catch (ReplaceImpossibleException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        
    }
    
    private final IStyledText styledText;
    private final Model model;
    private final StyledTextListener styledTextListener;
    
    private int fragmentStarts[];
    private int fragmentLengths[];
    private boolean fragmentHasStart[], fragmentHasEnd[];
    
    public ModelReconciler(IStyledText styledText, Model model) {
        if (styledText == null)
            throw new NullPointerException("styledText is null");
        if (model == null)
            throw new NullPointerException("model is null");
        this.styledText = styledText;
        this.model = model;
        
        List<Fragment> fragments = model.fragments();
        
        indexFragments(fragments);
        
        StringBuilder text = new StringBuilder();
        for (Fragment f : fragments) {
            text.append(f.toString());
        }
        styledText.setText(text.toString());
        
        model.addListener(this);
        
        styledTextListener = new StyledTextListener();
        styledText.addListener(SWT.Verify, styledTextListener);
        styledText.addListener(SWT.Modify, styledTextListener);
        
    }
    
    private void indexFragments(List<Fragment> fragments) {
        fragmentStarts = new int[fragments.size()];
        fragmentLengths = new int[fragments.size()];
        fragmentHasStart = new boolean[fragments.size()];
        fragmentHasEnd = new boolean[fragments.size()];
        int i = 0, start = 0;
        for (Fragment f : fragments) {
            fragmentStarts[i] = start;
            fragmentLengths[i] = f.toString().length();
            fragmentHasStart[i] = f.includesStart();
            fragmentHasEnd[i] = f.includesEnd();
            start += fragmentLengths[i];
            i++;
        }
    }
    
    public int findLeftFragment(int offset, boolean isDeletion) {
        int position = 0;
        int index = 0;
        for (int i = 0; i < fragmentStarts.length; i++) {
            int intervalStart = position + (fragmentHasStart[i] || isDeletion ? 0 : 1);
            int fLength = fragmentLengths[i];
            int intervalEnd = position + (fragmentHasEnd[i] ? 0 : -1) + fLength;
            
            // System.out.println("("+intervalStart+","+intervalEnd+")");
            
            if (offset < intervalStart - 1)
                return -1;
            
            if (offset >= intervalStart && offset <= intervalEnd)
                return index;
            
            position += fLength;
            index++;
        }
        return -1;
    }
    
    public int findRightFragment(int offset, boolean isDeletion) {
        int position = 0;
        int index = 0;
        int rightmost = -1;
        for (int i = 0; i < fragmentStarts.length; i++) {
            int intervalStart = position + (fragmentHasStart[i] ? 0 : 1);
            int fLength = fragmentLengths[i];
            int intervalEnd = position + (fragmentHasEnd[i] || isDeletion ? 0 : -1) + fLength;
            
            if (offset < intervalStart - 1)
                return rightmost;
            
            if (offset >= intervalStart && offset <= intervalEnd)
                rightmost = index;
            
            position += fLength;
            index++;
        }
        return rightmost;
    }
    
    /**
     * FIXME: speedup by caching fragment offsets
     */
    public int getFragmentOffset(int index) {
        // int pos = 0;
        // int num = 0;
        // for (int i = 0; i < fragmentLengths.length; i++) {
        // if (num == index)
        // return pos;
        // pos += fragment.toString().length();
        // num++;
        // }
        // return pos;
        return fragmentStarts[index];
    }
    
    public boolean canModify(int start, int end) {
        boolean isDeletion = start < end;
        int startFragment = findLeftFragment(start, isDeletion);
        int endFragment = findRightFragment(end, isDeletion);
        if (startFragment == -1 || endFragment == -1 || startFragment > endFragment)
            return false;
        int startOffset = start - getFragmentOffset(startFragment);
        boolean hasModifiableArea = false;
        for (int i = startFragment; i <= endFragment; i++) {
            int endOffset;
            Fragment fragment = model.fragments().get(i);
            if (i == endFragment) {
                endOffset = end - getFragmentOffset(endFragment);
            } else {
                endOffset = fragmentLengths[i];
            }
            startOffset = 0;
            if (fragment.canReplace(startOffset, endOffset - startOffset)) {
                hasModifiableArea = true;
            }
        }
        return hasModifiableArea;
    }
    
    public void modify(int start, int end, String text) throws ReplaceImpossibleException {
        if (text == null)
            throw new NullPointerException("text is null");
        boolean isDeletion = (start < end);
        int startFragment = findLeftFragment(start, isDeletion);
        int endFragment = findRightFragment(end, isDeletion);
        if (startFragment == -1 || endFragment == -1 || startFragment > endFragment)
            throw new ReplaceImpossibleException("Given text ranges are not available");
        int startOffset = start - getFragmentOffset(startFragment);
        for (int i = startFragment; i <= endFragment; i++) {
            int endOffset;
            Fragment fragment = model.fragments().get(i);
            if (i == endFragment)
                endOffset = end - getFragmentOffset(endFragment);
            else
                endOffset = fragmentLengths[i];
            if (fragment.canReplace(startOffset, endOffset - startOffset)) {
                model.replace(this, fragment, startOffset, endOffset - startOffset, text);
                text = "";
            }
            startOffset = 0;
        }
        if (!text.equals("")) {
            throw new ReplaceImpossibleException("Text ranges are not editable");
        }
    }
    
    public void modelChanged(Object sender, Model model, int firstFragment, int oldCount, int newCount) {
        int l = 0;
        final int startOffset = fragmentStarts[firstFragment];
        for (int i = firstFragment; i < firstFragment + oldCount; i++)
            l += fragmentLengths[i];
        final int cutLength = l;
        final StringBuilder newText = new StringBuilder();
        for (int i = firstFragment; i < firstFragment + newCount; i++) {
            newText.append(model.fragments().get(i));
        }
        
        indexFragments(model.fragments());
        final String text = styledText.getText();
        final String text2 = text.substring(0, startOffset) + newText
                + text.substring(startOffset + cutLength);
        Display.getDefault().syncExec(new Runnable() {
            
            public void run() {
                styledTextListener.disabled = true;
                if (!text.equals(text2)) {
                    // styledText.replaceTextRange(startOffset, cutLength,
                    // newText.toString());
                    styledText.setText(text2);
                }
                styledTextListener.disabled = false;
                
            }
            
        });
    }
    
    void dispose() {
        model.removeListener(this);
        styledText.removeListener(SWT.Verify, styledTextListener);
        styledText.removeListener(SWT.Modify, styledTextListener);
    }
    
}
