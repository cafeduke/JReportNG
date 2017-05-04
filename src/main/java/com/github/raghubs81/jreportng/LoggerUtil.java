package com.github.raghubs81.jreportng;

import static com.github.raghubs81.jreportng.ReportProperties.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * This class instantiates LoggerUtil.logger of type java.util.logging.Logger. LoggerUtil.logger
 * can be used by any test case of thread safe logging.
 * 
 * @author Raghunandan.Seshadri
 */  
public final class LoggerUtil
{
   /**
    * List of handlers that shall be added to the logger
    */
   public static List<Handler> LIST_HANDLER = new ArrayList<> (); 
   
   private LoggerUtil ()
   {
      
   }
   
   /**
    * Create a logger object using the fully qualified name of the class as logger name.
    * 
    * @param testClass Class that needs logger object. 
    * @return Logger object for the the testClass. A new object is not created if one already exists with the same name.
    */
   protected static Logger getLogger (Class<?> testClass)
   {
      String loggerName  = testClass.getName();      
      String logFileName = testClass.getName() + ".html";
      String logTitle    = testClass.getSimpleName();        
      return createLogger (loggerName, logFileName, logTitle);
   }
   
   /**
    * Create a logger with given {@code loggerName} to log details to file {@code logFilename} with HTML title {@code logTitle}.  
    * 
    * @param loggerName A logger object shall be created with this name.
    * @param logFileName The logger shall log entries to his file.
    * @param logTitle The title of the HTML log.
    * @return Logger object.
    */
   private static Logger createLogger (String loggerName, String logFileName, String logTitle)
   {
      Logger logger = Logger.getLogger (loggerName);
      setLoggerProperties (logger, logFileName, logTitle);
      return logger;
   }
   
   /**
    * Set logger properties for the {@code logger} to log in {@code logFileName}.
    * 
    * The logger is setup as follows
    * <ul>
    *    <li>If logger has handlers then its all set</li>
    *    <li>Set log level to {@link com.github.raghubs81.jreportng.LoggerUtil LoggerUtil}</li>
    *    <li>Create a file handler for file {@code DIR_JREPORT_TARGET_LOG/logFileName} </li>
    *    <li>Add HtmlFormatter with {@code logTitle} to the file hander</li>
    * </ul>
    * @param logger The logger to be setup.
    * @param logFileName HTML log filename.
    * @param logTitle The title for the HTML file.
    */
   private static void setLoggerProperties (Logger logger, String logFileName, String logTitle)
   {
      if (logger.getHandlers() != null && logger.getHandlers().length > 0)
         return;
         
      logger.setUseParentHandlers(false);
      logger.setLevel(JREPORT_LOG_LEVEL);
      Handler handler = null;
      try
      {
         DIR_JREPORT_TARGET_LOG.mkdirs();
         String logFilePath = new File (DIR_JREPORT_TARGET_LOG, logFileName).getAbsolutePath();
         handler = new FileHandler (logFilePath);
         handler.setLevel(JREPORT_LOG_LEVEL);
         handler.setFormatter(new HtmlFormatter(logTitle));
      }
      catch (IOException e)
      {
         throw new IllegalStateException ("Error setting log handler", e);
      }  
      logger.addHandler(handler);
      LIST_HANDLER.forEach((h) -> logger.addHandler(h));
   }
}
