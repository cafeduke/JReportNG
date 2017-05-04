package com.github.raghubs81.jreportng;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.logging.Logger;

public class LoggerFactory
{
   public static Logger getJReportLogger (Class<?> testClass)
   {
      Logger logger = LoggerUtil.getLogger(testClass);
      JReportLogUtil.updateLogReport(testClass);
      return logger;
   }
   
   protected static void injectLogger (Class<?> testClass)
   {
      injectLogger (getJReportLogger(testClass), testClass);
   }
   
   protected static void injectLogger (Logger logger, Class<?> testClass)
   {
      Arrays.asList(testClass.getDeclaredFields())
         .stream  ()
         .filter  ((f) -> (f.getType() == Logger.class))
         .filter  ((f) -> f.isAnnotationPresent(JReportLogger.class))
         .filter  ((f) -> Modifier.isStatic(f.getModifiers()))    
         .forEach ((f) -> setLoggerOnField(f, logger, testClass));
   }   
   
   protected static void setLoggerOnField (Field f, Logger logger, Class<?> testClass)
   {
      try
      {
         f.set (null, logger);
      }
      catch (Exception e)
      {
         throw new IllegalStateException("Exception injecting logger in class " + testClass.getName());
      }
   }   
}
