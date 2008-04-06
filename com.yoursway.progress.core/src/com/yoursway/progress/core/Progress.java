package com.yoursway.progress.core;

public interface Progress extends BasicProgressElement {
    
    void setTaskName(String name);
    
    Progress subtask(double weight, Naming naming);
    
    ItemizedProgress items(int items);
    
    ItemizedProgress items(int items, double totalWeigth);

    void checkCancellation();
    
    void done();
    
    void allocate(double totalWeigth);
    
    void worked(double weight);
    
    void skip(double weight);

}
