package com.github.raghubs81.jreportng;

import java.util.logging.*;
import org.testng.annotations.*;
import com.github.raghubs81.jreportng.JReportLogger;

public class ClassA extends AbstractTestCase
{   
   @JReportLogger
   protected static Logger logger;
   
   @BeforeClass
   public static void setup ()
   {
      TestUtil.sleepInMilli (101);
      System.out.println("BeforeClass");
      logger.info ("BeforeClass");
   }
   
   @Test
   public void testA1 ()
   {
      TestUtil.sleepInMilli (201);
      System.out.println("testA1");
      logger.info ("testA1");
   }

   @Test
   public void testA2 ()
   {
      TestUtil.sleepInMilli (301);
      System.out.println("testA2");
      logger.info ("testA2");
   }
   
   @Test
   public void testA3 ()
   {
      TestUtil.sleepInMilli (201);
      System.out.println("testA3");
      logger.info ("testA3");
   }   
   
   @AfterClass
   public static void clean()
   {
      TestUtil.sleepInMilli (101);
      System.out.println("AfterClass");
      logger.info ("AfterClass");
   }   
}