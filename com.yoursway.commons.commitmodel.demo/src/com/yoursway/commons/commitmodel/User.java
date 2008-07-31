package com.yoursway.commons.commitmodel;

public class User {
    
    private final String name;

    public User(String name) {
        if (name == null)
            throw new NullPointerException("name is null");
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
}
