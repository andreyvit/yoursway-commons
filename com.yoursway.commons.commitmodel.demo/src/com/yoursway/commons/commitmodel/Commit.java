package com.yoursway.commons.commitmodel;

public class Commit {
    
    private final Tree tree;
    private final User user;

    public Commit(User user, Tree tree) {
        if (user == null)
            throw new NullPointerException("user is null");
        if (tree == null)
            throw new NullPointerException("tree is null");
        this.user = user;
        this.tree = tree;
    }
    
}
