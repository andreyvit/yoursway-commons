package com.yoursway.swt.coolsidebar;

import static com.google.common.collect.Lists.newLinkedList;

import java.util.List;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.yoursway.swt.coolsidebar.viewmodel.SidebarItem;
import com.yoursway.swt.coolsidebar.viewmodel.SidebarModel;
import com.yoursway.swt.coolsidebar.viewmodel.SidebarSection;
import com.yoursway.swt.scrollbar.CoolScrolledComposite;

public class CoolSidebar {
    
    private final CoolScrolledComposite scrolledComposite;
    private final Canvas canvas;
    public SidebarModel model;
    
    public List<SidebarViewChild> children = newLinkedList();
    
    public CoolSidebar(Composite parent) {
        final Color activeBackground = new Color(parent.getDisplay(), 214, 221, 229);
        final Color inactiveBackground = new Color(parent.getDisplay(), 232, 232, 232);
        
        scrolledComposite = new CoolScrolledComposite(parent, SWT.NONE);
        scrolledComposite.setBackground(inactiveBackground);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        
        canvas = new Canvas(scrolledComposite.parentComposite(), SWT.NONE);
        scrolledComposite.setContent(canvas);
        
        canvas.setBackground(inactiveBackground);
        
        parent.getShell().addShellListener(new ShellListener() {
            
            public void shellActivated(ShellEvent e) {
                scrolledComposite.setBackground(activeBackground);
                canvas.setBackground(activeBackground);
            }
            
            public void shellClosed(ShellEvent e) {
                // nothing
            }
            
            public void shellDeactivated(ShellEvent e) {
                scrolledComposite.setBackground(inactiveBackground);
                canvas.setBackground(inactiveBackground);
            }
            
            public void shellDeiconified(ShellEvent e) {
                // nothing
            }
            
            public void shellIconified(ShellEvent e) {
                // nothing
            }
            
        });
        
        GridLayoutFactory.fillDefaults().spacing(0, 0).generateLayout(canvas);
    }
    
    public void setLayoutData(Object layoutData) {
        scrolledComposite.setLayoutData(layoutData);
    }
    
    public void setModel(SidebarModel model) {
        if (model == null)
            throw new NullPointerException("model is null");
        
        this.model = model;
        
        children.clear();
        for (SidebarSection section : model.sections()) {
            children.add(new SectionTitleView(section, canvas));
            addItemViews(section.children());
        }
        
        canvas.layout();
        scrolledComposite.redraw();
    }
    
    private void addItemViews(List<SidebarItem> items) {
        for (SidebarItem item : items) {
            children.add(new ItemView(item, canvas));
            addItemViews(item.children());
        }
    }
}
