package org.dew.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestSAMLSP extends TestCase {
  
  public TestSAMLSP(String testName) {
    super(testName);
  }
  
  public static Test suite() {
    return new TestSuite(TestSAMLSP.class);
  }
  
  public void testApp() throws Exception {
    System.out.println("org.dew.test.TestSAMLSP");
  }
}
