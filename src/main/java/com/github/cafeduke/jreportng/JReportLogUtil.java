package com.github.cafeduke.jreportng;

import static com.github.cafeduke.jreportng.ReportProperties.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * A utility class that initializes reporting directory structure and links the HTML log file
 * for a given test class resulting in Javadocs like directory structure.
 *  
 * @author Raghunandan.Seshadri
 */
public class JReportLogUtil
{
   /**
    * A map of package name to the classes it contains.
    */
   private static Map<String,TreeSet<String>> mapPackToClasses = new TreeMap <String, TreeSet<String>> ();
   
   static
   {
      TestListener.log("Started initializing report resources");
      initReportResources ();
      TestListener.log("Finished initializing report resources");
   }
   
   /**
    * Create resources (like css, images) required for HTML report in folder 
    * {@link com.github.cafeduke.jreportng.ReportProperties ReportProperties}.DIR_JREPORT_TARGET_LOG.
    */
   public static void initReportResources ()
   {
      if (new File (DIR_JREPORT_TARGET_LOG, "index.html").exists())
         return;
      
      try
      {
         DIR_JREPORT_TARGET_LOG.mkdirs();         
         
         for (String currResource : JREPORT_SOURCE_RESOURCE)
         {
            Path pathResource = Paths.get(currResource.replace(JREPORT_PREFIX, ""));
            String fileTarget = pathResource.getFileName().toString();            
            File dirTarget = new File (DIR_JREPORT_TARGET_LOG, pathResource.getParent().toString());
            
            if (!dirTarget.exists())
               dirTarget.mkdirs();
            
            Files.copy(JReportLogUtil.class.getResourceAsStream(currResource), new File (dirTarget, fileTarget).toPath(), StandardCopyOption.REPLACE_EXISTING);
         }
         
         JReportLogUtil.writeLogIndexHtml ();         
      }
      catch (Exception e)
      {
         throw new IllegalStateException ("Error setting up target report", e);
      }
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
         String defaultHtmlLog   = JREPORT_LOG_DEFAULT_CLASS.getName() + ".html";
         String defaultClassName = JREPORT_LOG_DEFAULT_CLASS.getSimpleName();
         
         // Create the default log file if it does not exist
         File fileHtmlDefaultLog = new File (DIR_JREPORT_TARGET_LOG, defaultHtmlLog);
         fileHtmlDefaultLog.createNewFile();
         
         File file = new File (DIR_JREPORT_TARGET_LOG, "index.html");
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
         
         file = new File (DIR_JREPORT_TARGET_LOG, "package-root.html");
         out = new PrintWriter (new FileWriter (file));
         out.println ("<html>");
         out.println ("<head>");
         out.println ("   <link type='text/css' rel='stylesheet' href='" + JREPORT_LOG_TESTCLASS_CSS + "' ></link>");
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
         File file = new File (DIR_JREPORT_TARGET_LOG, "package-" + packageName + ".html");
         PrintWriter out = new PrintWriter (new FileWriter (file));

         out.println ("<html>");
         out.println ("<head>");
         out.println ("   <link type='text/css' rel='stylesheet' href='" + JREPORT_LOG_TESTCLASS_CSS  + "' ></link>");
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
         File file = new File (DIR_JREPORT_TARGET_LOG, "packages.html");
         PrintWriter out = new PrintWriter (new FileWriter (file));

         out.println ("<html>");
         out.println ("<head>");
         out.println ("   <link type='text/css' rel='stylesheet' href='" + JREPORT_LOG_TESTCLASS_CSS + "'></link>");
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

}
