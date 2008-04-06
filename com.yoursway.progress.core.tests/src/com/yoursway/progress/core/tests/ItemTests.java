package com.yoursway.progress.core.tests;

import org.junit.Test;

import com.yoursway.progress.core.ItemizedProgress;

public class ItemTests extends AbstractTests {

    @Test
    public void simplyTwentyItems() {
        reporter.setSteps(3);
        ItemizedProgress items = progress.items(3);
        items.item();
        items.item();
        items.item();
        items.done();
        reporter.verify();
    }
    
    @Test
    public void skipOddEndingWithItem() {
        ItemizedProgress items = progress.items(5);
        items.item();
        items.skip();
        reporter.expect(1.0 / 5);
        items.item();
        reporter.expect(1.0 / 5);
        items.skip();
        reporter.expect(1.0 / 5 + (1.0 - 1.0 / 5) / 3);
        items.item();
        reporter.expect(1.0 / 5 + (1.0 - 1.0 / 5) / 3);
        items.done();
        reporter.verify();
    }
    
    @Test
    public void skipOddEndingWithSkip() {
        ItemizedProgress items = progress.items(4);
        items.item();
        items.skip();
        items.item();
        items.skip();
        items.done();
        reporter.verify();
    }
    
}
