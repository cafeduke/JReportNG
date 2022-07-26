package com.github.cafeduke.jreportng;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
public class InstanceBaseLoggerTest extends AbstractTestCase
{
    @BeforeClass
    public void setup()
    {
        TestUtil.sleepAndLog(logger, "BeforeClass", 101);
    }

    @Test
    public void testA()
    {
        TestUtil.sleepAndLog(logger, "testA", 301);
    }

    @Test
    public void testB()
    {
        TestUtil.sleepAndLog(logger, "testB", 301);
    }

    @AfterClass
    public void clean()
    {
        TestUtil.sleepAndLog(logger, "AfterClass", 101);
    }
}
