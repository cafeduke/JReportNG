package com.github.cafeduke.jreportng;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.logging.Level;

/**
 * This class hosts the properties used by JReport.  
 *  
 * @author Raghunandan.Seshadri
 */
public class ReportProperties
{
   /**
    * System properties override the default properties
    */
   private static Properties testProperties = System.getProperties();
   
   /**
    * Organization prefix in the package.
    * Used in reporting and removed in logs to make logs crisper.
    */
   public static final String PACKAGE_ORG_PREFIX = getDefaultProperty("jreport.org.prefix", "package-root");
   
   /**
    * JReport files are placed in {@link #DIR_JREPORT_TARGET_RESOURCE}{@code /JREPORT_PREFIX}
    */
   public static final String JREPORT_PREFIX = "/jreportng";
   
   /**
    * Path to resources that is relative to project base directory.
    */
   public static final File DIR_JREPORT_SOURCE_RESOURCE = new File ("src/main/resources" + JREPORT_PREFIX);   
   
   /**
    * The home directory for JReport files. Set using system property {@code jreport.home }.
    * Defaults to {@code target/jreport} - typical for a Maven project. 
    */
   public static final File DIR_JREPORT_TARGET_RESOURCE = new File (getDefaultProperty("jreport.home", "target" + JREPORT_PREFIX));
   
   /**
    * Path to the directory containing all HTML log files including index.html that links all the HTML logs.
    * Path = {@link #DIR_JREPORT_TARGET_RESOURCE}{@code /log}
    */
   public static final File DIR_JREPORT_TARGET_LOG = new File (DIR_JREPORT_TARGET_RESOURCE, "log");
   
   /**
    * Log level. Set using system property {@code jreport.loglevel}.
    */
   public static final Level JREPORT_LOG_LEVEL = Level.parse(getDefaultProperty("jreport.loglevel", "FINE"));
   
   /**
    * Date format. Set using system property {@code jreport.log.dateformat}. 
    */
   public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(getDefaultProperty("jreport.log.dateformat", "EEE, dd-MMM-yyyy HH:mm:ss.SSS z"));
   
   /**
    * The class to be used when the source class of the message is TestListener or not known.
    */
   public static final Class<?> JREPORT_LOG_DEFAULT_CLASS = TestListener.class;
   
   /**
    * Relative path to CSS file from HTML log.
    */
   public static final String JREPORT_LOG_TESTCLASS_CSS = "css/log-testclass.css";
   
   /**
    * Line separator
    */
   static final String LINE_SEP = System.getProperty("line.separator");
   
   /**
    * Resources in the JAR to be extracted to the the DIR_JREPORT_TARGET_LOG directory.
    */
   static final String JREPORT_SOURCE_RESOURCE [] = new String [] 
   {
      JREPORT_PREFIX + "/css/log-testclass.css",
      JREPORT_PREFIX + "/images/tableBGDarkBlue.jpg",
      JREPORT_PREFIX + "/images/headingBarBlue.jpg",
      JREPORT_PREFIX + "/images/headingBarLightGreen.jpg",
      JREPORT_PREFIX + "/images/headingBarLightOrange.jpg",
      JREPORT_PREFIX + "/images/headingBarLightRed.jpg",
      JREPORT_PREFIX + "/images/tableBGDarkBlue.jpg",
      JREPORT_PREFIX + "/images/tableBGDarkBrown.jpg",
      JREPORT_PREFIX + "/images/tableBGLightBlue.jpg"               
   };     
   
   private ReportProperties()
   {
      
   }

   /**
    * Return property value if property name {@code propertyName} is set. 
    * Otherwise return {@code defaultValue}.  
    * 
    * @param propertyName System property name.
    * @param defaultValue Default value if the system property is not set.
    * @return Property value if {@code propertyName} is set, {@code defaultValue} otherwise. 
    */
   public static String getDefaultProperty(String propertyName, String defaultValue)
   {
      String propertyValue = testProperties.getProperty(propertyName);
      if (propertyValue == null)
         propertyValue = defaultValue;      
      return propertyValue;
   }
   
   /**
    * A package name without the  {@link #PACKAGE_ORG_PREFIX}
    * 
    * @param packageName The package name to trim.
    * @return A package name without the  {@link #PACKAGE_ORG_PREFIX}
    */
   public static String getDisplayPackageName (String packageName)
   {
      return (packageName.equals(PACKAGE_ORG_PREFIX)) ? "." : packageName.replace(PACKAGE_ORG_PREFIX + ".", "");
   }
   
   /**
    * A class name without {@link #PACKAGE_ORG_PREFIX}
    * @param testClass The name of the test class shall be trimmed.
    * @return A class name without {@link #PACKAGE_ORG_PREFIX}
    */
   public static String getDisplayName (Class<?> testClass)
   {
      return testClass.getName().replace(PACKAGE_ORG_PREFIX + ".", "");
   }
}
