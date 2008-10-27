package com.yoursway.swt.coolsidebar.viewmodel;

import java.util.List;

public interface SidebarSection {
    
    String name();
    
    boolean collapsable();
    
    List<SidebarItem> children();
    
}
