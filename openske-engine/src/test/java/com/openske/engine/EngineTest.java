package com.openske.engine;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * OpenSKE's Engine Tests
 */
public class EngineTest extends TestCase {
    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public EngineTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(EngineTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testEngineInitialization() {
        assertTrue(true);
    }
}
