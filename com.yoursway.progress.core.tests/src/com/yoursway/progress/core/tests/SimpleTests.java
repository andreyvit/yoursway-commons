package com.yoursway.progress.core.tests;

import org.junit.Test;

public class SimpleTests extends AbstractTests {
    
    @Test
    public void jumpsToOneOnDone() {
        progress.done();
        reporter.verify();
    }

}
