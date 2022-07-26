package com.github.cafeduke.jreportng;

import java.util.logging.Logger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
public class LoggerFactoryTest extends AbstractTestCase
{
    @JReportLogger
    public static Logger logger = LoggerFactory.getJReportLogger(LoggerFactoryTest.class);

    @BeforeClass
    public static void setup()
    {
        TestUtil.sleepAndLog(logger, "BeforeClass", 21);
    }

    @Test
    public void test()
    {
        TestUtil.sleepAndLog(logger, "factory test", 101);
    }

    @AfterClass
    public static void clean()
    {
        TestUtil.sleepAndLog(logger, "AfterClass", 21);
    }
}
