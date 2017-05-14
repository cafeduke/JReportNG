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
    * JReport files are placed in {@link #DIR_REPORT_HOME}{@code /JREPORT_PREFIX}
    */
   public static final String JREPORT_PREFIX = "/jreportng";
   
   /**
    * Path to resources that is relative to project base directory.
    */
   public static final File DIR_SOURCE_RESOURCE = new File ("src/main/resources" + JREPORT_PREFIX);   
   
   /**
    * The home directory for JReport files. Set using system property {@code jreport.home }.
    * Defaults to {@code target/jreport} - typical for a Maven project. 
    */
   public static final File DIR_REPORT_HOME = new File (getDefaultProperty("jreport.home", "target" + JREPORT_PREFIX));
   
   /**
    * Path to the directory containing all HTML log files including index.html that links all the HTML logs.
    * Path = {@link #DIR_REPORT_HOME}{@code /log}
    */
   public static final File DIR_REPORT_LOG_HOME = new File (DIR_REPORT_HOME, "log");
   
   /**
    * Log level. Set using system property {@code jreport.loglevel}.
    */
   public static final Level LOG_LEVEL = Level.parse(getDefaultProperty("jreport.loglevel", "FINE"));
   
   /**
    * Date format. Set using system property {@code jreport.log.dateformat}. 
    */
   public static final SimpleDateFormat LOG_DATE_FORMAT = new SimpleDateFormat(getDefaultProperty("jreport.log.dateformat", "EEE, dd-MMM-yyyy HH:mm:ss.SSS z"));
   
   /**
    * The class to be used when the source class of the message is TestListener or not known.
    */
   public static final Class<?> LOG_DEFAULT_CLASS = TestListener.class;
   
   /**
    * Relative path to CSS file from HTML log.
    */
   public static final String LOG_TESTCLASS_CSS = "css/log-testclass.css";
   
   /**
    * Path to style sheet to customize ReportNG reporting
    */
   public static final String PATH_TO_CUSTOM_REPORTNG_CSS = DIR_REPORT_HOME + "/css/reportng-custom.css";
   
   /**
    * Line separator
    */
   static final String LINE_SEP = System.getProperty("line.separator");
   
   /**
    * Resources in the JAR to be extracted to the the DIR_JREPORT_TARGET_LOG directory.
    */
   static final String LIST_SOURCE_RESOURCE [] = new String [] 
   {
      JREPORT_PREFIX + "/index.html",
      JREPORT_PREFIX + "/title.html",
      JREPORT_PREFIX + "/css/overview.css",
      JREPORT_PREFIX + "/css/reportng-custom.css",
      JREPORT_PREFIX + "/images/bg01.gif",
      JREPORT_PREFIX + "/images/DukeSurf.png",
      JREPORT_PREFIX + "/images/headerBG.jpg",
      JREPORT_PREFIX + "/images/headingBarBlue.jpg",
      JREPORT_PREFIX + "/images/headingBarDarkBlue.jpg",
      JREPORT_PREFIX + "/images/headingBarDarkGray.jpg",
      JREPORT_PREFIX + "/images/headingBarDarkGreen.jpg",
      JREPORT_PREFIX + "/images/headingBarDarkOrange.jpg",
      JREPORT_PREFIX + "/images/headingBarDarkRed.jpg",
      JREPORT_PREFIX + "/images/headingBarLightGray.jpg",
      JREPORT_PREFIX + "/images/headingBarLightGreen.jpg",
      JREPORT_PREFIX + "/images/headingBarLightOrange.jpg",
      JREPORT_PREFIX + "/images/headingBarLightPurple.jpg",
      JREPORT_PREFIX + "/images/headingBarLightRed.jpg",
      JREPORT_PREFIX + "/images/tableBGBlue.jpg",
      JREPORT_PREFIX + "/images/tableBGBlueLight.jpg",
      JREPORT_PREFIX + "/images/tableBGDarkBlue.jpg",
      JREPORT_PREFIX + "/images/tableBGDarkBrown.jpg",
      JREPORT_PREFIX + "/images/tableBGLightBlue.jpg",
      JREPORT_PREFIX + "/images/tableBGLightGreen.jpg",
      JREPORT_PREFIX + "/jquery-ui/js/jquery-ui-1.9.1.custom.js",
      JREPORT_PREFIX + "/jquery-ui/js/jquery-ui-1.9.1.custom.min.js",
      JREPORT_PREFIX + "/jquery-ui/themes/start/jquery-ui-1.9.1.custom.css",
      JREPORT_PREFIX + "/jquery-ui/themes/start/jquery-ui-1.9.1.custom.min.css",
      JREPORT_PREFIX + "/jquery-ui/themes/start/images/ui-bg_flat_55_999999_40x100.png",
      JREPORT_PREFIX + "/jquery-ui/themes/start/images/ui-bg_flat_75_aaaaaa_40x100.png",
      JREPORT_PREFIX + "/jquery-ui/themes/start/images/ui-bg_glass_45_0078ae_1x400.png",
      JREPORT_PREFIX + "/jquery-ui/themes/start/images/ui-bg_glass_55_f8da4e_1x400.png",
      JREPORT_PREFIX + "/jquery-ui/themes/start/images/ui-bg_glass_75_79c9ec_1x400.png",
      JREPORT_PREFIX + "/jquery-ui/themes/start/images/ui-bg_gloss-wave_45_e14f1c_500x100.png",
      JREPORT_PREFIX + "/jquery-ui/themes/start/images/ui-bg_gloss-wave_50_6eac2c_500x100.png",
      JREPORT_PREFIX + "/jquery-ui/themes/start/images/ui-bg_gloss-wave_75_2191c0_500x100.png",
      JREPORT_PREFIX + "/jquery-ui/themes/start/images/ui-bg_inset-hard_100_fcfdfd_1x100.png",
      JREPORT_PREFIX + "/jquery-ui/themes/start/images/ui-icons_0078ae_256x240.png",
      JREPORT_PREFIX + "/jquery-ui/themes/start/images/ui-icons_056b93_256x240.png",
      JREPORT_PREFIX + "/jquery-ui/themes/start/images/ui-icons_d8e7f3_256x240.png",
      JREPORT_PREFIX + "/jquery-ui/themes/start/images/ui-icons_e0fdff_256x240.png",
      JREPORT_PREFIX + "/jquery-ui/themes/start/images/ui-icons_f5e175_256x240.png",
      JREPORT_PREFIX + "/jquery-ui/themes/start/images/ui-icons_f7a50d_256x240.png",
      JREPORT_PREFIX + "/jquery-ui/themes/start/images/ui-icons_fcd113_256x240.png",
      JREPORT_PREFIX + "/jquery/jquery-1.8.2.js",
      JREPORT_PREFIX + "/jquery/jquery-1.8.2.min.js",
      JREPORT_PREFIX + "/jqplot/jqplot.donutRenderer.min.js",
      JREPORT_PREFIX + "/jqplot/jquery.jqplot.min.css",
      JREPORT_PREFIX + "/jqplot/jquery.jqplot.min.js",
      JREPORT_PREFIX + "/log/css/log-testclass.css",
      JREPORT_PREFIX + "/log/images/headingBarBlue.jpg",
      JREPORT_PREFIX + "/log/images/headingBarLightGreen.jpg",
      JREPORT_PREFIX + "/log/images/headingBarLightOrange.jpg",
      JREPORT_PREFIX + "/log/images/headingBarLightRed.jpg",
      JREPORT_PREFIX + "/log/images/tableBGDarkBlue.jpg",
      JREPORT_PREFIX + "/log/images/tableBGDarkBrown.jpg",
      JREPORT_PREFIX + "/log/images/tableBGLightBlue.jpg"
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
