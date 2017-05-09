package com.github.cafeduke.jreportng;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * This class provides a static factory method to create a JReport logger object.
 * 
 * <p>
 * {@code Logger logger = LoggerFactory.getJReportLogger(MyClass.class); } 
 * 
 * @author Raghunandan.Seshadri
 */
public class LoggerFactory
{
   /**
    * The factory method to get a Logger object.
    * 
    * @param testClass The class object to be used to create a Logger.
    * @return The logger object.
    */
   public static Logger getJReportLogger (Class<?> testClass)
   {
      Logger logger = LoggerUtil.getLogger(testClass);
      JReportLogUtil.updateLogReport(testClass);
      return logger;
   }
   
   /**
    * Inject the Logger created for class {@code testClass} into the class {@code testClass}.
    * 
    * @param testClass The class object into which the Logger needs to be inject.
    */
   protected static void injectLogger (Class<?> testClass)
   {
      injectLogger (getJReportLogger(testClass), testClass);
   }

   /**
    * Inject the given logger into the class {@code testClass}.
    * 
    * @param logger The logger object to be injected.
    * @param testClass The class to inject the logger object.
    */
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
