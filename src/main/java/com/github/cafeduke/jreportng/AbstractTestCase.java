package com.github.cafeduke.jreportng;

import java.util.logging.Logger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * This class should be the base class (parent) of all JUnit4 test classes. <br>
 * 
 * <pre>
 * <code>
 *    class A extends AbstractTestCase
 *    {
 *        // A static logger object
 *        {@literal @}JReportLogger
 *        public static Logger logger;
 *            
 *        Test
 *        public void myTest ()
 *        {
 *           // logger object is injected from base class
 *           logger.info ("HelloWorld");
 *        }
 *    }
 * }
 * </code>
 * </pre>
 * 
 * OR
 * 
 * <pre>
 * <code>
 *    class A extends AbstractTestCase
 *    {
 *        Test
 *        public void myTest ()
 *        {
 *           // Instance logger object from base class
 *           logger.info ("HelloWorld");
 *        }
 *    }
 * </code>
 * </pre>
 * 
 * This class creates a built-in logger (java.util.logging.Logger) object. Test
 * watchers are added to log significant events.
 * 
 * @author Raghunandan.Seshadri
 */
public abstract class AbstractTestCase
{
    protected Logger logger;

    /**
     * A BeforeClass that marks the beginning of the test class, injects a static logger (if declared and annotated with {@code @JReportLogger})
     * into the derived logger.
     */
    @BeforeClass
    public void beginTestClass()
    {
        Class<?> testClass = getClass();
        String className = ReportProperties.getDisplayName(testClass);

        LoggerFactory.injectLogger(testClass);
        logger = LoggerFactory.getJReportLogger(testClass);
        logger.info("Started executing class " + className);
    }

    /**
     * An AfterClass that that marks the completion of the test class.
     */
    @AfterClass(alwaysRun = true)
    public void finishTestClass()
    {
        Class<?> testClass = getClass();
        String className = ReportProperties.getDisplayName(testClass);
        logger.info("Finished executing class " + className);
    }
}
