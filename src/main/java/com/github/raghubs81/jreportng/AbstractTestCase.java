package com.github.raghubs81.jreportng;

import java.util.logging.Logger;
import org.testng.annotations.*;

/**
 * This class should be the base class (parent) of all JUnit4 test classes. <br>
 * <blockquote>
 * 
 * <pre>
 *    class A extends AbstractTestCase
 *    {
 *        // A static logger object
 *        @JReportLogger
 *        protected static Logger logger;
 *            
 *        Test
 *        public void myTest ()
 *        {
 *           // logger object is injected from base class
 *           logger.info ("HelloWorld");
 *        }
 *    }
 * </pre>
 * 
 * OR
 * 
 * <pre>
 *    class A extends AbstractTestCase
 *    {
 *        Test
 *        public void myTest ()
 *        {
 *           // Instance logger object from base class
 *           logger.info ("HelloWorld");
 *        }
 *    }
 * </pre>
 * 
 * </blockquote>
 * 
 * This class creates a built-in logger (java.util.logging.Logger) object. Test
 * watchers are added to log significant events.
 * 
 * @author Raghunandan.Seshadri
 */
public abstract class AbstractTestCase
{  
   protected Logger logger;
   
   @BeforeClass
   public void beginTestClass ()
   {
      Class<?> testClass = getClass ();         
      String className = ReportProperties.getDisplayName(testClass);
      
      LoggerFactory.injectLogger(testClass);
      TestClassResultManager.getInstance(testClass).startedTestClass();
      
      logger = LoggerFactory.getJReportLogger(testClass);      
      logger.info ("Started executing class " + className);      
   }
   
   @AfterClass (alwaysRun=true)
   public final void finishTestClass ()
   {
      Class<?> testClass = getClass ();         
      String className = ReportProperties.getDisplayName(testClass);
      
      TestClassResultManager resultManager = TestClassResultManager.getInstance(testClass);
      resultManager.finishedTestClass();
      
      logger.info ("Finished executing class " + className + " in " + resultManager.getTimeTaken());
      logger.info ("Result: " + resultManager.getResultSummary());      
   }
}
