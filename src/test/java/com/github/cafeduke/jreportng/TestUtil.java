package com.github.cafeduke.jreportng;

import static com.github.cafeduke.jreportng.ReportProperties.*;

import java.io.File;

@SuppressWarnings("javadoc")
public class TestUtil
{
   public static final File DIR_JREPORT_TEST_RESOURCE = new File ("src/test/resources" + JREPORT_PREFIX);
   
   private TestUtil ()
   {
      
   }
   
   static void sleepInMilli (int milli)
   {
      try 
      {
         Thread.sleep(milli);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   
   static void sleep (int sec)
   {
      sleepInMilli(sec * 1000);
   }   
}
