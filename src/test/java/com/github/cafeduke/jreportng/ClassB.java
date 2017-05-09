package com.github.cafeduke.jreportng;

import java.util.logging.Logger;
import org.testng.annotations.*;

import com.github.cafeduke.jreportng.AbstractTestCase;
import com.github.cafeduke.jreportng.JReportLogger;

@SuppressWarnings("javadoc")
public class ClassB extends AbstractTestCase
{
   @JReportLogger
   public static Logger logger;
   
   @BeforeClass
   public static void setup ()
   {
      TestUtil.sleepInMilli (101);
      System.out.println("BeforeClass");
      logger.info ("BeforeClass");
   }
   
   @Test
   public void testB1 ()
   {
      TestUtil.sleepInMilli (301);
      System.out.println("testB1");
      logger.info ("testB1");
   }

   @Test
   public void testB2 ()
   {
      TestUtil.sleepInMilli (301);
      System.out.println("testB2");
      logger.info ("testB2");
   }
   
   @AfterClass
   public static void clean()
   {
      TestUtil.sleepInMilli (101);
      System.out.println("AfterClass");
      logger.info ("AfterClass");
   }   
}