package com.yoursway.fsmonitor.demo;

import static com.yoursway.swt.additions.FormDataBuilder.formDataOf;
import static com.yoursway.swt.additions.YsSwtUtils.centerShellOnNearestMonitor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.yoursway.fsmonitor.FileSystemChangesListener;
import com.yoursway.fsmonitor.FileSystemMonitor;
import com.yoursway.fsmonitor.FileSystemMonitoringContext;

public class FsMonitorDemo implements IApplication {
    
//    public static void main(String[] args) {
//        try {
//            System.out.println(System.getProperty("java.library.path"));
//            System.setProperty("java.library.path", System.getProperty("java.library.path")
//                    + File.pathSeparator
//                    + new File("../com.yoursway.fsmonitor.macosx.leopard").getCanonicalPath() + "/");
//            System.out.println(System.getProperty("java.library.path"));
//            new FileSystemMonitoringContext().run();
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }

    private FileSystemMonitoringContext context;
    
    class MonitorUI implements FileSystemChangesListener {
        
        private Text log;
        private Text text;
        private FileSystemMonitor monitor;
        private Button apply;
        private File folder;
        private Button newWindow;
        private Shell shell;

        public MonitorUI(final Display display) {
            shell = new Shell(display);
            shell.setLayout(new GridLayout(1, false));
            shell.setSize(600, 400);
            shell.setLayout(new GridLayout(4, false));
            
            folder = new File(System.getProperty("user.home"));

            Label label = new Label(shell, SWT.NONE);
            label.setText("Folder to monitor:");
            
            text = new Text(shell, SWT.SINGLE | SWT.BORDER);
            text.setText(folder.getPath());
            text.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    apply.setEnabled(!folder.getPath().equals(text.getText()));
                }
            });
            
            apply = new Button(shell, SWT.NONE);
            apply.setText("Apply");
            apply.setEnabled(false);
            apply.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    File newFolder = new File(text.getText());
                    if (!newFolder.isDirectory()) {
                        display.beep();
                        return;
                    }
                    folder = newFolder;
                    monitor.dispose();
                    createMonitor();
                    text.setText(folder.getPath());
                } 
            });
            
            newWindow = new Button(shell, SWT.NONE);
            newWindow.setText("New Monitor");
            newWindow.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    new MonitorUI(display);
                } 
            });
            
            log = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
            
            label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
            text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            apply.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
            newWindow.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
            log.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
            
            shell.setDefaultButton(apply);
            
            createMonitor();
            
            shell.addDisposeListener(new DisposeListener() {

                public void widgetDisposed(DisposeEvent e) {
                    monitor.dispose();
                }
                
            });
            
            if (display.getShells().length == 1)
                centerShellOnNearestMonitor(shell);
            
            shell.open();
        }

        private void updateTitle() {
            shell.setText("File System Monitor - " + folder.getPath());
        }

        private void createMonitor() {
            monitor = new FileSystemMonitor(context, folder, this);
            updateTitle();
            addToLog("monitoring " + folder);
        }

        public void changed(String path) {
            addToLog(path);
        }

        private void addToLog(String message) {
            ScrollBar verticalBar = log.getVerticalBar();
            Point selection = log.getSelection();
            boolean emptySelection = selection.x == selection.y;
            boolean scrolledToBottom = verticalBar.getSelection() == verticalBar.getMaximum();
            
            Calendar now = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = format.format(now.getTime());
            
            log.setRedraw(false);
            log.append("[" + date + "] " + message + "\n");
            
            if (emptySelection)
                log.setSelection(log.getText().length());
            if (scrolledToBottom)
                verticalBar.setSelection(verticalBar.getMaximum());
            log.setRedraw(true);
        }

        public void inoperational() {
        }

        public void operational() {
        }
        
    }

    public Object start(IApplicationContext context0) throws Exception {
        context = new FileSystemMonitoringContext();
        Display display = new Display();
        
        new MonitorUI(display);
        
        while (!display.isDisposed() && display.getShells().length > 0) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        context.dispose();
        display.dispose();
        return IApplication.EXIT_OK;
    }

    public void stop() {
    }
    
}
