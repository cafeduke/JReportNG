package com.github.cafeduke.jreportng;

import org.testng.*;

import com.github.cafeduke.jreportng.TestClassResultManager.MethodResultState;

import static com.github.cafeduke.jreportng.ReportProperties.*;

import java.util.logging.*;

/**
 * A TestListener that handles test class and test method life cycle events. 
 * 
 * @author Raghunandan.Seshadri
 */
public class TestListener implements ITestListener, ISuiteListener, IExecutionListener, IConfigurationListener
{
   
   /**
    * --------------------------------------------------------
    *          Listener - TestNG Level
    * --------------------------------------------------------
    */      
   
   @Override
   public void onExecutionFinish()
   {
      String mesg = "Started executing TestNG instance";
      log (mesg);      
      JReportLogUtil.initReportResources();      
   }

   @Override
   public void onExecutionStart()
   {
      String mesg = "Finished executing TestNG instance";
      log (mesg);
   }   
   
   /**
    * --------------------------------------------------------
    *          Listener - Test Suite Level
    * --------------------------------------------------------
    */      
   
   @Override
   public void onStart(ISuite suite)
   {
      String mesg = "Started executing suite " + suite.getName();  
      log (mesg);      
   }

   @Override
   public void onFinish(ISuite suite)
   {
      String mesg = "Finished executing suite " + suite.getName();
      log (mesg);
   }
   
   /**
    * --------------------------------------------------------
    *          Listener - Test Element (package) level
    * --------------------------------------------------------
    */

   @Override
   public void onStart(ITestContext arg0)
   {
   }   

   @Override
   public void onFinish(ITestContext arg0)
   {
   }
   
   /**
    * --------------------------------------------------------
    *          Listener - Test Method Level
    * --------------------------------------------------------
    */    
   
   @Override
   public void onTestStart(ITestResult result)
   {
      TestClassResultManager resultManager = TestClassResultManager.getInstance(result.getTestClass().getRealClass());
      resultManager.startedTestMethod(result.getMethod().getMethodName());
      
      String mesg = "Started executing test " + TestListener.getDisplayName(result);
      log (mesg, result, Level.INFO, HtmlFormatter.STYLE_ROW_HIGHLIGHT);
   }

   @Override
   public void onTestSuccess(ITestResult result)
   {
      TestClassResultManager resultManager = TestClassResultManager.getInstance(result.getTestClass().getRealClass());
      resultManager.setTestMethodState(result.getMethod().getMethodName(), MethodResultState.PASSED);
      
      String mesg = "Test " + TestListener.getDisplayName(result) + " passed";
      log (mesg, result, Level.INFO, HtmlFormatter.STYLE_ROW_SUCCESS);
   }

   @Override
   public void onTestSkipped(ITestResult result)
   {
      TestClassResultManager resultManager = TestClassResultManager.getInstance(result.getTestClass().getRealClass());
      resultManager.setTestMethodState(result.getMethod().getMethodName(), MethodResultState.SKIPPED);
      
      String mesg = "Test " + TestListener.getDisplayName(result) + " skipped";
      log(mesg, result, Level.WARNING, HtmlFormatter.STYLE_ROW_WARNING);
   }

   @Override
   public void onTestFailure(ITestResult result)
   {
      TestClassResultManager resultManager = TestClassResultManager.getInstance(result.getTestClass().getRealClass());
      resultManager.setTestMethodState(result.getMethod().getMethodName(), MethodResultState.FAILED);
      
      String mesg = "Test " + TestListener.getDisplayName(result) + " failed";
      log(mesg, result, Level.SEVERE, result.getThrowable());
   }

   @Override
   public void onTestFailedButWithinSuccessPercentage(ITestResult result)
   {
      // TODO Auto-generated method stub
      
   }

   /**
    * --------------------------------------------------------
    *                Listener - Test Configuration Level
    * --------------------------------------------------------
    */     

   @Override
   public void onConfigurationFailure(ITestResult result)
   {
      ITestNGMethod method = result.getMethod();
      String prefix = "[" + result.getMethod().getRealClass().getSimpleName() + "] ";
      String message = prefix + (method.isBeforeClassConfiguration() ? "Before Class failed" : "After Class failed");
      
      log (message, result, Level.INFO, HtmlFormatter.STYLE_ROW_FAILURE);
      log ("StackTrace", result, Level.SEVERE,  result.getThrowable());
   }

   @Override
   public void onConfigurationSkip (ITestResult result)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void onConfigurationSuccess(ITestResult result)
   {
      ITestNGMethod method = result.getMethod();
      String prefix = "[" + result.getMethod().getRealClass().getSimpleName() + "] ";
      String message = prefix + (method.isBeforeClassConfiguration() ? "Before Class passed" : "After Class passed");
      
      log (message, result, Level.INFO, HtmlFormatter.STYLE_ROW_SUCCESS);
   }   
   
   /**
    * A trim version made up of package, class and method name extracted from  
    * {@link org.testng.ITestResult ITestResult} 
    * 
    * @param result The ITestResult object.
    * @return A trim display name that includes package, class and method name.
    */
   public static String getDisplayName (ITestResult result)
   {
      Class<?> testClass = (result == null || result.getTestClass()  == null) ? JREPORT_LOG_DEFAULT_CLASS : result.getTestClass().getRealClass();
      String  testMethod = (result == null || result.getMethod() == null)     ? "" : result.getMethod().getMethodName();
      String testClassName = ReportProperties.getDisplayName(testClass);      
      return testClassName + "-" + testMethod;
   }
   
   /**
    * Invoke log (mesg, null, Level.INFO, null, null)
    *                   
    * @see #log(String, ITestResult, Level, String, Throwable)
    * @param mesg Message to be logged
    */
   public static void log (String mesg)
   {
      log (mesg, null, Level.INFO, null, null);
   }

   /**
    * Invoke log (mesg, ITestResult, Level.INFO, null, null)
    *                   
    * @see #log(String, ITestResult, Level, String, Throwable)
    * @param mesg Message to be logged
    * @param result The ITestResult object.
    */
   public static void log (String mesg, ITestResult result)
   {
      log (mesg, result, Level.INFO, null, null);
   }
   
   /**
    * Invoke log (mesg, ITestResult, level, styleClass, null);
    * @see #log(String, ITestResult, Level, String, Throwable)
    * 
    * @param mesg Message to be logged
    * @param result The ITestResult object.
    * @param level The log level.
    * @param styleClass The style class among the ones in HtmlFormatter
    */
   public static void log (String mesg, ITestResult result, Level level, String styleClass)
   {
      log (mesg, result, level, styleClass, null);
   }
   
   /**
    * Invoke log (mesg, ITestResult, level, null, e);
    * @see #log(String, ITestResult, Level, String, Throwable)
    * 
    * @param mesg Message to be logged
    * @param result The ITestResult object.
    * @param level The log level.
    * @param thrown The stack trace of the Throwable to be logged.
    */
   public static void log (String mesg, ITestResult result, Level level, Throwable thrown)
   {
      log (mesg, result, level, null, thrown);
   }
   
   /**
    * Get meta-data details like test class, test method from {@code description}, get logger for the test class
    * and log the {@code mesg}.
    * 
    * <ul>
    *   <li>Test class is extracted from {@code description}, if test class is null then JREPORT_LOG_DEFAULT_CLASS is used</li>
    *   <li>A logger is obtained based on test class</li>
    *   <li>If styleClass or thrown exists, then the same is used for logging.
    * </ul>
    * 
    * @param mesg Message to be logged
    * @param result The ITestResult object.
    * @param level The log level.
    * @param styleClass The style class among the ones in HtmlFormatter
    * @param thrown The stack trace of the Throwable to be logged.
    */
   public static void log (String mesg, ITestResult result, Level level, String styleClass, Throwable thrown)
   {
      Class<?> testClass = (result == null || result.getTestClass()  == null) ? JREPORT_LOG_DEFAULT_CLASS : result.getTestClass().getRealClass();
      String  testMethod = (result == null || result.getMethod() == null) ? "" : result.getMethod().getMethodName();
      Logger logger = LoggerUtil.getLogger(testClass);
      
      if (styleClass == null)
      {
         if (thrown == null)
            logger.logp (level, testClass.getName(), testMethod, mesg);
         else
            logger.logp (level, testClass.getName(), testMethod, mesg, thrown);
      }
      else
         logger.logp (level, testClass.getName(), testMethod, mesg, new String[]{styleClass});
   }
}
