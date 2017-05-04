package com.github.raghubs81.jreportng;

import java.util.logging.Logger;
import org.testng.annotations.*;
import com.github.raghubs81.jreportng.AbstractTestCase;
import com.github.raghubs81.jreportng.JReportLogger;

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
