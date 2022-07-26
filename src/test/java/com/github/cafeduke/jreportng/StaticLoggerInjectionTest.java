package com.github.cafeduke.jreportng;

import java.util.logging.Logger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
public class StaticLoggerInjectionTest extends AbstractTestCase
{
    @JReportLogger
    public static Logger logger;

    @BeforeClass
    public static void setup()
    {
        TestUtil.sleepAndLog(logger, "BeforeClass", 51);
    }

    @Test
    public void testP()
    {
        TestUtil.sleepAndLog(logger, "testP", 201);
    }

    @Test
    public void testQ()
    {
        TestUtil.sleepAndLog(logger, "testQ", 401);
    }

    @AfterClass
    public static void clean()
    {
        TestUtil.sleepAndLog(logger, "AfterClass", 51);
    }

}
