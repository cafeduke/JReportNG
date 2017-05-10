package com.github.cafeduke.jreportng;

import java.util.logging.*;
import org.testng.annotations.*;

import com.github.cafeduke.jreportng.AbstractTestCase;
import com.github.cafeduke.jreportng.JReportLogger;

public class StaticLoggerInjectionTest extends AbstractTestCase
{   
   @JReportLogger
   public static Logger logger;
   
   @BeforeClass
   public static void setup ()
   {
      TestUtil.sleepAndLog (logger, "BeforeClass", 51);
   }
   
   @Test
   public void testP ()
   {
      TestUtil.sleepAndLog (logger, "testP", 201);
   }

   @Test
   public void testQ ()
   {
      TestUtil.sleepAndLog (logger, "testQ", 401);
   }
   
   @AfterClass
   public static void clean()
   {
      TestUtil.sleepAndLog (logger, "AfterClass", 51);
   }   
   
}