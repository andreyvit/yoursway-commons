package com.yoursway.swt.coolsidebar.viewmodel;

import java.util.List;

public interface SidebarItem {
    
    SidebarIcon icon();
    
    String title();
    
    List<SidebarItem> children();
    
}
