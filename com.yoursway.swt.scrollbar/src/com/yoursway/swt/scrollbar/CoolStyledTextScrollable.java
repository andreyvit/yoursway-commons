package com.yoursway.swt.scrollbar;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class CoolStyledTextScrollable extends Composite {
    
    private final StyledText styledText;
    private CoolScrollBar vScrollBar;
    private CoolScrollBarStyledTextBinding binding;
    
    public CoolStyledTextScrollable(Composite parent, StyledText styledText) {
        super(parent, SWT.NONE);
        if (styledText == null)
            throw new IllegalArgumentException("styledText is null");
        styledText.setParent(this);
        this.styledText = styledText;
        setupControls();
    }
    
    private void setupControls() {
        styledText.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL)
                .create());
        
        vScrollBar = new CoolScrollBar(this, SWT.NONE, true);
        vScrollBar.setLayoutData(GridDataFactory.fillDefaults().grab(false, true).create());
        
        GridLayoutFactory.fillDefaults().numColumns(2).spacing(1, 0).generateLayout(this);
        this.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        
        binding = new CoolScrollBarStyledTextBinding(styledText, vScrollBar, this);
    }
    
    public CoolStyledTextScrollable(Composite parent, int style) {
        super(parent, SWT.NONE);
        this.styledText = new StyledText(this, style);
        setupControls();
    }
    
    public StyledText styledText() {
        return styledText;
    }
    
    public void updateScrollbarPosition() {
        binding.updateScrollbarPosition();
    }
    
}
