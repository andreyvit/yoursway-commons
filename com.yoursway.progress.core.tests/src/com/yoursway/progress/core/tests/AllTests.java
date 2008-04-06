package com.yoursway.progress.core.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses( { SimpleTests.class, WorkerTests.class, SubtaskTests.class, ItemTests.class })
public class AllTests {
}
