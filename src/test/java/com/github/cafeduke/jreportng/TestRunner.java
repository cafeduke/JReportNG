package com.github.cafeduke.jreportng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlSuite.FailurePolicy;

@SuppressWarnings("javadoc")
public class TestRunner
{
    static
    {
        System.setProperty("jreport.org.prefix", "com.github.raghubs81");
    }

    public static void main(String arg[])
    {
        TestNG testNG = new TestNG();
        testNG.setVerbose(1);
        testNG.setConfigFailurePolicy(FailurePolicy.CONTINUE);
        testNG.setListenerClasses(Arrays.asList(TestListener.class));

        List<String> listSuite = new ArrayList<String>();
        listSuite.add(TestUtil.DIR_JREPORT_TEST_RESOURCE + "/Suite.xml");
        testNG.setTestSuites(listSuite);
        testNG.run();
    }
}
