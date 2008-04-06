package com.yoursway.progress.core;

public interface ItemizedProgress extends BasicProgressElement {
    
    void item();
    
    void item(double weight);
    
    void item(String itemName);
    
    void item(String itemName, double weight);
    
    void skip();
    
    void skip(double weight);
    
    void done();
    
    Progress subtask(double weight, Naming naming);
    
    Progress subtask();

}
