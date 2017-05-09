package com.github.cafeduke.jreportng;

import java.lang.annotation.*;

/**
 * Annotate a static field with {@code @JReportLogger} and the base class shall inject the logger.
 * 
 * <pre>
 * <code>
 *    class A extends AbstractTestCase
 *    {
 *        // A static logger object
 *        {@literal @}JReportLogger
 *        protected static Logger logger;
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
 * Note that this is required only if we need a static logger object. The base class instantiates a {@code logger} object with {@code protected} visibility 
 * and is inherited by the derived class.
 * 
 * @see AbstractTestCase
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JReportLogger
{

}
