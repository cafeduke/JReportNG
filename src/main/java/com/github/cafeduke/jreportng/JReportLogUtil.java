package com.github.cafeduke.jreportng;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import static com.github.cafeduke.jreportng.ReportProperties.*;

/**
 * A utility class that initializes reporting directory structure and links the HTML log file
 * for a given test class resulting in Javadocs like directory structure.
 *  
 * @author Raghunandan.Seshadri
 */
public class JReportLogUtil
{
   /**
    * Total number of tests that have passed (Summation of multiple TestNG runs).
    */
   private static int testRunPass = 0;

   /**
    * Total number of tests that have failed (Summation of multiple TestNG runs).
    */
   private static int testRunFail = 0;

   /**
    * Total number of tests that have skipped (Summation of multiple TestNG runs).
    */
   private static int testRunSkip = 0;
   
   /**
    * Track the start and end time of the test execution
    */
   private static LocalDateTime startTime, endTime;
   
   /**
    * A map of package name to the classes it contains.
    */
   private static Map<String,TreeSet<String>> mapPackToClasses = new TreeMap <String, TreeSet<String>> ();
   
   static
   {
      TestListener.log("Started initializing report resources");
      handleTestRunStart();
      TestListener.log("Finished initializing report resources");
   }

   /**
    * Update map of classes by adding <b>testClass</b> under appropriate package.
    *
    * <ul>
    *    <li> Extract the package name for <b>testClass</b>
    *    <li> If the package name is not found in map of classes, add one and rewrite 
    *         {@code packages.html - This has links for all packages }.   
    *    <li> Rewrite {@code package-<package name>.html - This has links for all classes in this package} which
    *         will now include a link for <b>testClass</b> as well.
    * </ul>
    * @param testClass The test class to be updated in the log report.
    */
   public synchronized static void updateLogReport (Class<?> testClass)
   {
      String packageName = testClass.getPackage().getName();
      String className   = testClass.getSimpleName();
      
      TreeSet<String> setClasses = mapPackToClasses.get(packageName);
      if (setClasses == null)
      {
         setClasses = new TreeSet<String> ();         
         mapPackToClasses.put(packageName, setClasses);
         writeAllPackagesHtml ();
      }
      
      if (!setClasses.contains(className))
      {
         setClasses.add(className);
         writePackageHtml (packageName);
      }
   }
   
   /**
    * Write the index file for all the logs.
    * 
    * The index file divides the view area into 3 frames similar to Javadoc.
    * <ol>
    *    <li> The list of packages
    *    <li> The list of classes for the selected package
    *    <li> Log entries for the selected class.
    * </ol>
    */
   private static void writeLogIndexHtml ()
   {
      try
      {
         String defaultHtmlLog   = LOG_DEFAULT_CLASS.getName() + ".html";
         String defaultClassName = LOG_DEFAULT_CLASS.getSimpleName();
         
         // Create the default log file if it does not exist
         File fileHtmlDefaultLog = new File (DIR_REPORT_LOG_HOME, defaultHtmlLog);
         fileHtmlDefaultLog.createNewFile();
         
         File file = new File (DIR_REPORT_LOG_HOME, "index.html");
         PrintWriter out = new PrintWriter (new FileWriter (file));
         out.println ("<html>");
         out.println ("<frameset cols='15%,*'>");
         out.println ("   <frameset rows='40%,*'>");
         out.println ("      <frame name='packageNames' src='packages.html'/>");
         out.println ("      <frame name='classNames'   src='package-root.html'/>");
         out.println ("   </frameset>");
         out.println ("   <frame name='content' src='" + defaultHtmlLog + "'/>");
         out.println ("<frameset>");
         out.println ("</html>");         
         out.close();
         
         file = new File (DIR_REPORT_LOG_HOME, "package-root.html");
         out = new PrintWriter (new FileWriter (file));
         out.println ("<html>");
         out.println ("<head>");
         out.println ("   <link type='text/css' rel='stylesheet' href='" + LOG_TESTCLASS_CSS + "' ></link>");
         out.println ("</head>");
         out.println ("<body>");
         out.println ("<table class='tableLogLink'>");
         out.println ("<tr><td class='noWrapColumn'>" +
                         "<a href='" + defaultHtmlLog + "' target='content'>" + defaultClassName +
                      "</td></tr>");
         out.println ("</table>");
         out.println ("</body>");
         out.println ("</html>");
         out.close();         
         out.close();
      }
      catch (IOException ioe)
      {
         throw new IllegalStateException("Error writing log index", ioe);
      }
   }
   
   /**
    * Write the  {@code package-<package name>.html} which contains links for all classes in given package.
    * 
    * @param packageName Name of the package.
    */
   private static void writePackageHtml (String packageName)
   {
      try
      {
         File file = new File (DIR_REPORT_LOG_HOME, "package-" + packageName + ".html");
         PrintWriter out = new PrintWriter (new FileWriter (file));

         out.println ("<html>");
         out.println ("<head>");
         out.println ("   <link type='text/css' rel='stylesheet' href='" + LOG_TESTCLASS_CSS  + "' ></link>");
         out.println ("</head>");
         out.println ("<body>");
         out.println ("<table class='tableLogLink'>");
         
         for (String currClass : mapPackToClasses.get(packageName))
            out.println ("<tr><td class='noWrapColumn'>" +
                            "<a href='" + packageName + "." + currClass + ".html' target='content'>" + currClass + "" +
                         "</td></tr>");
               
         out.println ("</table>");
         out.println ("</body>");
         out.println ("</html>");
         out.close();
      }
      catch (IOException ioe)
      {
         throw new IllegalStateException("Error writing package file ", ioe);
      }      
   }
   
   /**
    * Write the  {@code packages.html} which contains links for all packages.
    */
   private static void writeAllPackagesHtml ()
   {
      try
      {
         File file = new File (DIR_REPORT_LOG_HOME, "packages.html");
         PrintWriter out = new PrintWriter (new FileWriter (file));

         out.println ("<html>");
         out.println ("<head>");
         out.println ("   <link type='text/css' rel='stylesheet' href='" + LOG_TESTCLASS_CSS + "'></link>");
         out.println ("</head>");
         out.println ("<body>");
         out.println ("<table class='tableLogLink'>");
         out.println ("<tr><td class='noWrapColumn'>&nbsp</td></tr>");
         
         out.println ("<tr><td class='noWrapColumn'>" +
                         "<b><a href='package-root.html' target='classNames'>" + PACKAGE_ORG_PREFIX + " </b>" +
                      "</td></tr>");         
         for (String packageName : mapPackToClasses.keySet())
         {
            String displayPackageName = getDisplayPackageName(packageName);
            out.println ("<tr><td class='noWrapColumn'>" +
                            "<a href='package-" + packageName + ".html' target='classNames'>" + displayPackageName + "" +
                         "</td></tr>");
         }
         
         out.println ("</table>");
         out.println ("</body>");
         out.println ("</html>");
         out.close();
      }
      catch (IOException ioe)
      {
         throw new IllegalStateException("Error writing all packages file", ioe);
      }
   }
   
   /**
    * Initialize Reporting
    */
   public static void handleTestRunStart()
   {      
      startTime = LocalDateTime.now();
      System.setProperty("org.uncommons.reportng.stylesheet", PATH_TO_CUSTOM_REPORTNG_CSS);
      
      if (new File (DIR_REPORT_LOG_HOME, "index.html").exists())
         return;
      
      setupJReportResources ();
   }

   /**
    * Setup maven target directory DIR_REPORT_HOME with resources.
    */
   private static void setupJReportResources ()
   {
      try
      {
         // Create report log home directory
         DIR_REPORT_LOG_HOME.mkdirs();         
         
         // Copy all the source resources to maven target report home
         for (String currResource : LIST_SOURCE_RESOURCE)
         {
            Path pathResource = Paths.get(currResource.replace(JREPORT_PREFIX, ""));
            String fileTarget = pathResource.getFileName().toString();            
            File dirTarget = new File (DIR_REPORT_HOME, pathResource.getParent().toString());
            
            if (!dirTarget.exists())
               dirTarget.mkdirs();
            
            Files.copy(JReportLogUtil.class.getResourceAsStream(currResource), new File (dirTarget, fileTarget).toPath(), StandardCopyOption.REPLACE_EXISTING);
         }

         // Create an index file for all the HTML logs   
         JReportLogUtil.writeLogIndexHtml ();         
      }
      catch (Exception e)
      {
         throw new IllegalStateException ("Error setting up report", e);
      }
   }
   
   /**
    * Complete reporting.
    * Add pages with details that are known at the end of test execution.
    */
   public static void handleTestRunCompletion ()
   {
      try
      {
         endTime = LocalDateTime.now();
         setupOverviewHtml ();
      }
      catch (IOException e)
      {
         throw new IllegalStateException ("Error completing report", e);
      }
   }
   
   /**
    * Add results from the {@code suite} to the total test result.
    * 
    * @param suite Suites that completed run.
    */
   public static void handleTestSuitesCompletion (ISuite suite)
   {
      Map<String,ISuiteResult> mapStringResult = suite.getResults();      
      for (String suiteName : mapStringResult.keySet())
      {
         ITestContext context = mapStringResult.get(suiteName).getTestContext();
         testRunPass += context.getPassedTests().size();
         testRunFail += context.getFailedTests().size();
         testRunSkip += context.getSkippedTests().size();
      }
   }    
   
   /**
    * Setup the overview.html
    */
   private static void setupOverviewHtml () throws IOException
   {
      int pieChartItemValue[] = new int[] {testRunPass, testRunFail, testRunSkip};
      
      File fileOverview = new File (DIR_REPORT_HOME, "overview.html");
      PrintWriter out = new PrintWriter (new FileWriter (fileOverview));      
      out.println ("<html>");
      out.println ("<head>   ");
      
      out.println ("<link type='text/css' rel='stylesheet' href='css/overview.css'></link>");
      out.println ("<link type='text/css' rel='stylesheet' href='jquery-ui/themes/start/jquery-ui-1.9.1.custom.css'></link>");    
      out.println ("<link type='text/css' rel='stylesheet' href='jqplot/jquery.jqplot.min.css' />");
      
      out.println ("<script type='text/javascript' src='jquery/jquery-1.8.2.min.js'></script>");
      out.println ("<script type='text/javascript' src='jquery-ui/js/jquery-ui-1.9.1.custom.min.js'></script>");
      out.println ("<script type='text/javascript' src='jqplot/jquery.jqplot.min.js'></script>");
      out.println ("<script type='text/javascript' src='jqplot/jqplot.donutRenderer.min.js'></script>");
      out.println ("");
      
      out.println ("<script>");
      out.println ("$(document).ready(function()");
      out.println ("{");
      out.println (String.format("   var s1 = [['fail',%d], ['skip',%d], ['pass',%d]];", testRunFail, testRunSkip, testRunPass));
      out.println ("   var plot3 = $.jqplot('chart', [s1], ");
      out.println ("   {");
      out.println ("      animate: true,");
      out.println ("      seriesColors: ['rgb(200,0,0)','rgb(255,128 ,0)','rgb(0,128,64)'],");
      out.println ("      seriesDefaults: ");
      out.println ("      {");
      out.println ("         renderer:$.jqplot.DonutRenderer,");
      out.println ("         rendererOptions:");
      out.println ("         {");
      out.println ("            sliceMargin: 3,");
      out.println ("            startAngle: 0,");
      out.println ("            showDataLabels: true,");
      out.println ("            dataLabels: 'value'");
      out.println ("         }");
      out.println ("      },");
      out.println ("      grid: ");
      out.println ("      {");
      out.println ("         background:'rgba(255,255,255,0)',");
      out.println ("         borderWidth: 0,");
      out.println ("         shadow: false");
      out.println ("      }");
      out.println ("   });");
      out.println ("});");
      out.println ("");
      
      out.println ("$( ");
      out.println ("   function ()");
      out.println ("   {");
      out.println ("      $('#accordion').accordion({ header: 'div#accordionTitle'});");
      out.println ("   }");
      out.println (");");
      out.println ("");
      out.println ("</script>");
      
      out.println ("<style>");
      out.println ("   .jqplot-data-label { color:rgb(230,230,230);    }");
      out.println ("   #chart             { width:450px; height:450px; }");
      out.println ("</style>");
      
      out.println ("</head>");
      out.println ("<body>");

      out.println ("");
      out.println ("<table class='layoutTable'>");
      out.println ("<tr class='overview'>");
      out.println ("<td width='60%'>        ");
      writeResultSummarySection (out, pieChartItemValue);
      out.println ("</td>");
      out.println ("<td width='40%' valign='top'>");
      writeAccordionSection(out);
      out.println ("</td>");
      out.println ("</tr>");
      out.println ("</table>");
      out.println ("</body>");
      out.println ("</html>");     
      out.close();
   }
   
   private static void writeResultSummarySection (PrintWriter out, int pieChartItemValue[])
   {
      final String pieChartItemName  [] = new String [] {"Pass", "Fail", "Skip"};
      final String pieChartItemStyle [] = new String [] {"pass", "fail", "skip"}; 
      
      int pieChartItemSum = 0;
      for (int currItem : pieChartItemValue)
         pieChartItemSum += currItem;      
      
      out.println ("   <table class='stretch'>");
      out.println ("   <tr height='70%'>");
      out.println ("   <td>");
      
      out.println ("      <table class='stretch'>");
      out.println ("      <tr>");
      out.println ("         <td align='right'><img src='images/duke.png' height='250px'></div></td>");
      out.println ("         <td align='center'><div id='chart' class='posCenter'></div></td>");
      out.println ("      </tr>");
      out.println ("      </table>");

      out.println ("   </td>");
      out.println ("   </tr>");
      
      out.println ("   <tr height='30%'>");
      out.println ("   <td>");
      out.println ("      <table class='resultTable posCenter'>");
      out.println ("      <tr><th>&nbsp    </th><th> Count </th><th> Percent </th></tr>");
      for (int i = 0; i < pieChartItemName.length; ++i)
      {
         double percent = (pieChartItemSum == 0) ? 0 : ((double)pieChartItemValue[i] / pieChartItemSum) * 100;
         String strPercent = String.format("%3.2f", percent) + "%";
         out.println ("      <tr>" +
               "<th class='" +  pieChartItemStyle[i] + "'>" + pieChartItemName[i] + "</th>" +
               "<td>" + pieChartItemValue[i] + "</td>" +
               "<td>" + strPercent + "</td>" +
               "</tr>");
      }      
      out.println ("      </table>");
      out.println ("   </td>");
      out.println ("   </tr>");
      out.println ("   </table>");  
   }
   
   /**
    * Write the test environment details as HTML accordion. 
    * 
    * @param out Writer to add the HTML section.
    */
   private static void writeAccordionSection (PrintWriter out)
   {
      long duration  = Duration.between(startTime, endTime).toMillis() / 1000;
      long hrs = duration / 3600;
      long min = (duration % 3600) / 60;
      long sec = (duration % 60);      
      String strDuration = String.format ("%02d : %02d : %02d", hrs, min, sec);      
      out.println (" ");
      out.println ("   <!-- Accordion -->");
      out.println ("   <div id='accordion'>");
      
      // Regress Information
      out.println ("      <div id='accordionTitle'>Regress Info</div>");
      out.println ("      <div>");
      out.println ("         <table>");
      out.println ("            <tr><th>Start Time</th><td colspan='2'>" + startTime.format(DateTimeFormatter.ISO_DATE_TIME)   + "</td></tr>");
      out.println ("            <tr><th>End Time  </th><td colspan='2'>" + endTime.format(DateTimeFormatter.ISO_DATE_TIME)     + "</td></tr>");
      out.println ("            <tr><th>Duration  </th><td colspan='2'>" + strDuration + "</td></tr>");
      out.println ("         </table>");
      out.println ("      </div>");
      
      // Java Runtime Information
      out.println ("      <div id='accordionTitle'>Java Runtime</div>");
      out.println ("      <div>");
      out.println ("         <table>");
      out.println ("            <tr><th>Java Version             </th><td colspan='2'>" + System.getProperty("java.version") + "</td></tr>");
      out.println ("            <tr><th>Java Home                </th><td colspan='2'>" + System.getProperty("java.home")    + "</td></tr>");
      out.println ("            <tr><th>Java Class Version       </th><td colspan='2'>" + System.getProperty("java.class.version") + "</td></tr>");
      out.println ("         </table>");
      out.println ("      </div>");
      
      // Platform Information
      out.println ("      <div id='accordionTitle'>Platform</div>");
      out.println ("      <div>");
      out.println ("         <table>");
      out.println ("            <tr><th>Operating System         </th><td colspan='2'>" + System.getProperty("os.name")      + "</td></tr>");
      out.println ("            <tr><th>Operating System Version </th><td colspan='2'>" + System.getProperty("os.version")   + "</td></tr>");      
      out.println ("            <tr><th>Architecture             </th><td colspan='2'>" + System.getProperty("os.arch") + "</td></tr>");
      out.println ("         </table>");
      out.println ("      </div>");
      
      out.println ("   </div>");
      out.println ("");
   }
   
}
