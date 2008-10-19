package com.yoursway.completion.demo;

import static org.eclipse.swt.SWT.MULTI;
import static org.eclipse.swt.SWT.SHELL_TRIM;
import static org.eclipse.swt.SWT.WRAP;

import java.io.IOException;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.yoursway.completion.gui.CompletionController;
import com.yoursway.document.Document;
import com.yoursway.document.DocumentPosition;

public class TextEditor {
    private Shell shell;
    private StyledText text;
    
	public static void main(String[] args) {
		Display display = new Display();
		TextEditor appl = new TextEditor(display);
		while (!appl.shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
	
	
    private TextEditor(final Display display) {
    	shell = new Shell(display,SHELL_TRIM);
        text = new StyledText(shell,MULTI|WRAP);
		
        
		FillLayout layout = new FillLayout();
		shell.setLayout(layout);
		try {
			new CompletionController(text, new DictionaryCompletion());
		} catch (IOException e) {
			e.printStackTrace();
		}
		shell.open();
    }
    
} 
