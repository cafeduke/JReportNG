package com.github.cafeduke.jreportng;

import java.util.logging.*;
import org.testng.annotations.*;

import com.github.cafeduke.jreportng.AbstractTestCase;
import com.github.cafeduke.jreportng.JReportLogger;

public class LoggerFactoryTest extends AbstractTestCase
{   
   @JReportLogger
   public static Logger logger = LoggerFactory.getJReportLogger(LoggerFactoryTest.class);
   
   @BeforeClass
   public static void setup ()
   {
      TestUtil.sleepAndLog (logger, "BeforeClass", 21);
   }
   
   @Test
   public void test ()
   {
      TestUtil.sleepAndLog (logger, "factory test", 101);
   }

   @AfterClass
   public static void clean()
   {
      TestUtil.sleepAndLog (logger, "AfterClass", 21);
   }   
}