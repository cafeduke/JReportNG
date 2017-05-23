package com.github.cafeduke.jreportng;

import java.util.logging.Logger;
import org.testng.annotations.*;
import com.github.cafeduke.jreportng.AbstractTestCase;
import com.github.cafeduke.jreportng.JReportLogger;

@SuppressWarnings("javadoc")
public class InstanceLoggerTest extends AbstractTestCase
{
   @JReportLogger
   public static Logger logger;
   
   @BeforeClass
   public static void setup ()
   {
      TestUtil.sleepAndLog (logger, "BeforeClass", 101);
   }
   
   @Test
   public void testA ()
   {
      TestUtil.sleepAndLog (logger, "testA", 301);
   }

   @Test
   public void testB ()
   {
      TestUtil.sleepAndLog (logger, "testB", 301);
   }
   
   @AfterClass
   public static void clean()
   {
      TestUtil.sleepAndLog (logger, "AfterClass", 101);
   }   
}