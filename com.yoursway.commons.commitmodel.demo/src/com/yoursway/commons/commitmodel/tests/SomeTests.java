package com.yoursway.commons.commitmodel.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

import com.yoursway.commons.commitmodel.Branch;
import com.yoursway.commons.commitmodel.Commit;
import com.yoursway.commons.commitmodel.Repository;
import com.yoursway.commons.commitmodel.Tree;
import com.yoursway.commons.commitmodel.User;
import com.yoursway.commons.commitmodel.demo.properties.PropertyBag;
import com.yoursway.commons.commitmodel.demo.properties.PropertyBagUpdateSession;
import com.yoursway.commons.commitmodel.demo.properties.StringProperty;
import com.yoursway.commons.commitmodel.demo.properties.state.PropertyBagState;
import com.yoursway.utils.dependencies.DependentCodeRunner;

public class SomeTests {
    
    private StringWriter buf;
    private PrintWriter out;
    
    @Before
    public void createBuffer() {
        buf = new StringWriter();
        out = new PrintWriter(buf);
    }
    
    protected void assertOutput(String output) {
        System.out.println(output);
        assertEquals(output.trim(), buf.toString().trim());
    }
    
    @Test
    public void reallySimpleUpdate() throws Exception {
        Repository repository = new Repository();
        Branch branch = new Branch(repository);
        User andreyvit = new User("andreyvit");
        
        Tree tree = new Tree(1, 42);
        Commit commit1 = new Commit(andreyvit, tree);
        
        branch.commit(commit1);
        
        PropertyBag pineBag = new PropertyBag(new PropertyBagState());
        final StringProperty foo = new StringProperty(pineBag, "foo");
        
        new DependentCodeRunner(new Runnable() {
            public void run() {
                out.println("foo = " + foo.value());
            }
        });
        
        PropertyBagUpdateSession session = new PropertyBagUpdateSession(pineBag);
        foo.update("bar", session);
        
        session.commit(andreyvit);
        
        assertOutput("foo = null\n" + "foo = bar\n");
    }
    
    @Test
    public void multiBranchSync() throws Exception {
        Repository repository = new Repository();
        Branch pineBranch = new Branch(repository);
        User andreyvit = new User("andreyvit");
        
        Tree tree = new Tree(1, 42);
        Commit commit1 = new Commit(andreyvit, tree);
        
        pineBranch.commit(commit1);
        
        Branch firBranch = new Branch(repository);
        
        PropertyBag pineBag = new PropertyBag(new PropertyBagState());
        final StringProperty pineFoo = new StringProperty(pineBag, "foo");
        
        new DependentCodeRunner(new Runnable() {
            public void run() {
                out.println("foo = " + pineFoo.value());
            }
        });
        
        PropertyBagUpdateSession session = new PropertyBagUpdateSession(pineBag);
        pineFoo.update("bar", session);
        
        session.commit(andreyvit);
        
        fail("Test not created yet :-)");
        assertOutput("foo = null\n" + "foo = bar\n");
    }
    
}
