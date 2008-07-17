package com.yoursway.swt.inspector;

import static com.yoursway.swt.additions.YsSwtUtils.applyMiniSize;
import static com.yoursway.swt.additions.YsSwtUtils.applySmallSize;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SwtInspectorView {
    
    private final Shell shell;
    private final SwtInspectorViewCallback callback;
    
    private Button highlightWidgetsButton;
    private Text infoText;
    private final SwtInspectorModel model;
    private Label trackingTipLabel;
    
    public SwtInspectorView(Display display, SwtInspectorViewCallback callback, SwtInspectorModel model) {
        if (display == null)
            throw new NullPointerException("display is null");
        if (callback == null)
            throw new NullPointerException("callback is null");
        if (model == null)
            throw new NullPointerException("model is null");
        this.callback = callback;
        this.model = model;
        
        shell = new Shell(display, SWT.TITLE | SWT.TOOL | SWT.CLOSE | SWT.BORDER | SWT.RESIZE | SWT.ON_TOP | SWT.NO_FOCUS);
        shell.setText("SWT Inspector");
        shell.setLayout(new GridLayout(1, false));
        
        createContents();
        update();
        
        shell.setSize(350, 600);
        addListeners();
    }
    
    private void addListeners() {
        highlightWidgetsButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                callback.setHighlightControls(highlightWidgetsButton.getSelection());
            }
        });
        shell.addDisposeListener(new DisposeListener() {
            
            public void widgetDisposed(DisposeEvent e) {
                callback.viewClosed();
            }
            
        });
    }
    
    public void update() {
        if (shell.isDisposed())
            return;
        setButtonSelection(highlightWidgetsButton, model.highlightControls);
        trackingTipLabel.setVisible(model.highlightControls);
    }
    
    private void setButtonSelection(Button button, boolean selection) {
        if (button.getSelection() != selection)
            button.setSelection(selection);
    }
    
    public void setMessage(String message) {
        infoText.setText(message);
    }
    
    public void open() {
        shell.open();
    }
    
    private void createContents() {
        highlightWidgetsButton = new Button(shell, SWT.CHECK | SWT.NO_FOCUS);
        highlightWidgetsButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        highlightWidgetsButton.setText("Highlight and inspect controls under the mouse");
        
        trackingTipLabel = new Label(shell, SWT.WRAP);
        trackingTipLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        trackingTipLabel
                .setText("To stop tracking and keep the text below unchanged, move or click your mouse with ALT+SHIFT pressed.");
        
        infoText = new Text(shell, SWT.MULTI | SWT.LEAD | SWT.BORDER);
        infoText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        infoText.setText("");
        
        Label openTip = new Label(shell, SWT.WRAP);
        openTip.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        openTip.setText("Alt-Shift-F2 opens this inspector");
        
        applySmallSize(shell);
        applyMiniSize(trackingTipLabel);
        applyMiniSize(openTip);
    }
    
    public void setMessageToWelcome() {
        setMessage("Welcome to the SWT Inspector!");
    }
    
}
