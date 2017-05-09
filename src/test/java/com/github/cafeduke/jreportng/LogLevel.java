package com.github.cafeduke.jreportng;

import java.util.logging.Logger;
import org.testng.annotations.*;

import com.github.cafeduke.jreportng.AbstractTestCase;
import com.github.cafeduke.jreportng.JReportLogger;

@SuppressWarnings("javadoc")
public class LogLevel extends AbstractTestCase
{
   @JReportLogger
   protected static Logger logger;
   
   @Test
   public void test ()
   {
      logger.severe("Severe");
      logger.warning("Warning");
      logger.info("Info");
      logger.fine("Fine");
      logger.finer("Finer");
      logger.finest("Finest");
   }
}
