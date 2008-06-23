package com.yoursway.swt.animations.demo;

import static com.yoursway.swt.additions.YsSwtUtils.centerShellOnNearestMonitor;
import static org.eclipse.jface.layout.GridDataFactory.swtDefaults;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.yoursway.swt.animations.flip.Flipper;
import com.yoursway.swt.animations.flip.StackLayoutFlipperListener;

public class FlipDemoShell {
    
    private Shell shell;
    
    private Composite stack;
    
    private Composite pages[] = new Composite[2];
    
    private int currentPage = 0;
    
    private Font smallFont;
    
    public FlipDemoShell(Display display) {
        if (display == null)
            throw new NullPointerException("display is null");
        
        FontData data = display.getSystemFont().getFontData()[0];
        data.setHeight(9);
        smallFont = new Font(display, data);
        
        shell = new Shell(display);
        shell.setLayout(new GridLayout(1, false));
        shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        
        stack = new Composite(shell, SWT.NO_BACKGROUND);
        stack.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        stack.setLayout(new StackLayout());
        
        pages[0] = createMainComposite(stack);
        pages[1] = createSettingsComposite(stack);
        ((StackLayout) stack.getLayout()).topControl = pages[currentPage];
        
        shell.setSize(500, 300);
        shell.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                disposeResources();
            }
        });
    }
    
    void disposeResources() {
        smallFont.dispose();
    }
    
    private Composite createMainComposite(Composite parent) {
        final Composite formComposite = new Composite(parent, SWT.NONE);
        formComposite.setLayout(new GridLayout(1, false));
        addBorder(formComposite);
        
        Table table = new Table(formComposite, SWT.SINGLE | SWT.FULL_SELECTION);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        
        TableColumn nameColumn = new TableColumn(table, SWT.LEAD);
        nameColumn.setText("Name");
        
        new TableItem(table, SWT.NONE).setText(0, "Zebra");
        new TableItem(table, SWT.NONE).setText(0, "Monkey");
        new TableItem(table, SWT.NONE).setText(0, "Tiger");
        new TableItem(table, SWT.NONE).setText(0, "Leopard");
        new TableItem(table, SWT.NONE).setText(0, "Puma");
        new TableItem(table, SWT.NONE).setText(0, "Snow Leopard");
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumn(i).pack();
        }
        
        Button settingsButton = new Button(formComposite, SWT.PUSH);
        settingsButton.setText("Flip");
        settingsButton.setLayoutData(swtDefaults().indent(0, 8).create());
        settingsButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                flip();
            }
        });
        
        return formComposite;
    }
    
    private void addBorder(final Composite composite) {
        composite.addPaintListener(new PaintListener() {
            
            public void paintControl(PaintEvent e) {
                GC gc = e.gc;
                Rectangle clientArea = composite.getClientArea();
                gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLACK));
                gc.drawRectangle(clientArea.x, clientArea.y, clientArea.width - 1, clientArea.height - 1);
                //                gc.drawRectangle(clientArea.x+1, clientArea.y+1, clientArea.width - 3, clientArea.height - 3);
            }
            
        });
    }
    
    private Composite createSettingsComposite(Composite parent) {
        Composite formComposite = new Composite(parent, SWT.NONE);
        formComposite.setLayout(new GridLayout(1, false));
        addBorder(formComposite);
        
        formComposite.setFont(smallFont);
        
        Label label = new Label(formComposite, SWT.NONE);
        label.setText("Working sets to show:");
        
        new Button(formComposite, SWT.CHECK).setText("YourSway IDE");
        new Button(formComposite, SWT.CHECK).setText("SADR");
        new Button(formComposite, SWT.CHECK).setText("EskoArtwork");
        
        Button settingsButton = new Button(formComposite, SWT.PUSH);
        settingsButton.setText("Done");
        settingsButton.setLayoutData(swtDefaults().indent(0, 8).create());
        settingsButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                flip();
            }
        });
        
        for (Control control : formComposite.getChildren())
            control.setFont(formComposite.getFont());
        
        return formComposite;
    }
    
    public Shell open() {
        centerShellOnNearestMonitor(shell);
        shell.open();
        return shell;
    }
    
    void flip() {
        Flipper flipper = new Flipper(pages[currentPage], pages[1 - currentPage], 370);
        new StackLayoutFlipperListener(flipper, stack);
        flipper.flip();
        currentPage = 1 - currentPage;
    }
    
}
