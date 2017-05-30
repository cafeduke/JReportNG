package com.github.cafeduke.jreportng;

import static com.github.cafeduke.jreportng.ReportProperties.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;
import org.apache.commons.lang.exception.ExceptionUtils;


/**
 * This class is used to format log as HTML.
 * 
 * @author Raghunandan.Seshadri
 */ 
public class HtmlFormatter extends Formatter
{
   /**
    * Table row style for success.
    */
   public static final String STYLE_ROW_SUCCESS = "logRowSucccess";
   
   /**
    * Table row style for failure.
    */
   public static final String STYLE_ROW_FAILURE = "logRowFailure";
   
   /**
    * Table row style for warning.
    */
   public static final String STYLE_ROW_WARNING = "logRowWarning";
   
   /**
    * Table row style for highlight.
    */
   public static final String STYLE_ROW_HIGHLIGHT  = "logRowResult";
   
   /**
    * Table row style for meta-data.
    */
   public static final String STYLE_ROW_METADATA = "logMetaData";   
   
   /**
    * Date formatter
    */
   private SimpleDateFormat formatter = LOG_DATE_FORMAT;   
  
   /**
    * HTML page title
    */
   private String title = "";
   
   /**
    * Create a Html Formatter with <b>title</b>
    * 
    * @param title HTML title.
    */
   public HtmlFormatter (String title)
   {
      this.title = title;
   }
   
   /**
    * @return Current date and time in access log format.
    */
   public String getDateTimeString()
   {
      return formatter.format(new Date());
   }   

   /**
    * {@inheritDoc}
    */   
   @Override
   public String format(LogRecord record)
   {
      StringBuilder builder = new StringBuilder ();
      String styleColumn [] = getColumnStyles (record);
      String column [] = getColumns (record);
      
      int indexMessage = column.length - 1;
      String message   = column[indexMessage];
      
      String attribName = " name='Thread-" + record.getThreadID() + "'"; 
      
      builder.append ("<tr" + attribName + getMetaDataRowStyleClass () + ">");      
      for (int i = 0; i < column.length - 1; ++i)
      {
         String currColumnStyle = styleColumn[i].equals("") ? "" : " " + styleColumn[i];
         builder.append("<td" + currColumnStyle + ">" + column[i] + "</td>");
      }
      builder.append ("</tr>" +  LINE_SEP);
      
      builder.append ("<tr" + attribName + getRowStyleClass (record) + ">");
      builder.append ("<td colspan='" + indexMessage + "' " + styleColumn[indexMessage] +">" + message + "</td>");
      builder.append ("</tr>" +  LINE_SEP);
      
      Throwable throwable = record.getThrown();
      if (throwable != null)
      {
         builder.append ("<tr" + attribName + ">" +  LINE_SEP);
         builder.append ("<td colspan='"+ column.length + "'>" +  LINE_SEP);
         builder.append(getTraceTable(throwable));
         builder.append ("</td>" +  LINE_SEP);
         builder.append ("</tr>" + LINE_SEP);
      }
      
      return builder.toString();
   } 
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getHead(Handler h)
   {
      final String tableHeadingStyle  = "class='logRowHeading'";
      
      StringBuilder builder = new StringBuilder ();
      builder.append ("<?xml version='1.0' encoding='utf-8' ?>" + LINE_SEP);
      builder.append ("<!doctype html>" + LINE_SEP);
      builder.append ("<html>" + LINE_SEP);
      builder.append ("<head>" + LINE_SEP);
      builder.append ("   <meta http-equiv='Content-Type' content='text/html;charset=utf-8' />" + LINE_SEP);
      builder.append ("   <link rel='stylesheet' type='text/css' href='" + LOG_TESTCLASS_CSS + "' />" + LINE_SEP);
      builder.append ("</head>" + LINE_SEP);
      builder.append ("<body>" + LINE_SEP);
      
      builder.append ("<h1>" + title + "</h1>" + LINE_SEP);
      builder.append ("<hr>" + LINE_SEP);      
      
      builder.append ("<div class='logContent'>" + LINE_SEP);
      builder.append ("<table class='tableLog'>" + LINE_SEP);
      builder.append ("<tr " + tableHeadingStyle + ">");
      builder.append ("<th>Date</th>");
      builder.append ("<th>Verbosity</th>");
      builder.append ("<th>Class</th>");
      builder.append ("<th>Method</th>");
      builder.append ("<th>ThreadId</th>");
      builder.append ("</tr>" + LINE_SEP);
      return builder.toString();
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getTail(Handler h)
   {
      StringBuilder builder = new StringBuilder ();      
      builder.append ("</table>" + LINE_SEP);
      builder.append ("</div>" + LINE_SEP);
      builder.append ("</body>" + LINE_SEP);
      builder.append ("</html>" + LINE_SEP);
      return builder.toString();
   }
   
   /**
    * @return The style class used for meta-data row
    */
   private String getMetaDataRowStyleClass ()
   {
      return " class='" + STYLE_ROW_METADATA + "'";
   }
   
   /**
    * @param record LogRecord used by Logger.
    * @return Style class for the entire row.
    */
   private String getRowStyleClass (LogRecord record)
   {
      Object param[] = record.getParameters();
      
      String style = "";
      if (param == null || param.length == 0)
      {
         Level currLevel = record.getLevel();
         if (currLevel == Level.SEVERE)
            style = " class='" + STYLE_ROW_FAILURE + "'";
         else if (currLevel == Level.WARNING)
            style = " class='" + STYLE_ROW_WARNING + "'";
      }
      else
      {
         style = " class='" + param[0].toString () + "'";
      }
      return style;
   }

   /**
    * @param record LogRecord used by Logger.
    * @return Value of each column
    */
   private String[] getColumns (LogRecord record)
   {
      /* Date */
      //SimpleDateFormat dateFormat = new SimpleDateFormat ("EEE, dd-MMM-yyyy HH:mm:ss.SSS z");
      String strDate = getDateTimeString();
      
      /* Level */
      String strLogLevel = record.getLevel().toString();
      
      /* Class */
      String strClass    = getDisplayPackageName(record.getSourceClassName());
      
      /* Method */
      String strMethod   = record.getSourceMethodName();
      
      /* ThreadID */
      String strThreadID = "" + record.getThreadID();
      
      /* Message */
      String strMessage  = record.getMessage();
      strMessage = strMessage.replaceAll(LINE_SEP, "<br>");
      
      String field [] = {strDate, strLogLevel, strClass, strMethod, strThreadID, strMessage};
      return field;
   }

   /**
    * 
    * @param record LogRecord used by Logger.
    * @return Style class for each column
    */
   private String[] getColumnStyles (LogRecord record)
   {
      String styleDate    = "class='noWrapColumn'";
      String styleMessage = "";

      String styleLogLevel = "";   
      Level logLevel = record.getLevel();
      if (logLevel == Level.SEVERE)
         styleLogLevel = "class='logColumnSevere'";
      else if (logLevel == Level.WARNING)
         styleLogLevel = "class='logColumnWarning'";
      
      String style [] = {styleDate, styleLogLevel, "", "", "", styleMessage};
      return style;
   }
   
   /**
    * @param throwable Throwable object having exception details.
    * @return  HTML table having stack trace.
    */
   private String getTraceTable (Throwable throwable)
   {
      final String oddTableStyle  = " class='traceOddRow'";
      final String evenTableStyle = " class='traceEvenRow'";
         
      
      StringBuilder builder = new StringBuilder ();
      builder.append ("<table class='tableStacktrace'>"  +  LINE_SEP);      
      builder.append("<tr" + oddTableStyle + "><td><b>StackTrace</b></td></tr>" +  LINE_SEP);
      
      builder.append("<tr><td>" + throwable.toString() + "</td></tr>" +  LINE_SEP);
      String trace[] = ExceptionUtils.getStackFrames(throwable);
      
      int count = 0;
      for (String currTrace : trace)
      {
         String currStyle = (++count % 2 == 1) ? oddTableStyle : evenTableStyle; 
         builder.append("<tr" + currStyle + "><td>" + currTrace + "</td></tr>" +  LINE_SEP);
      }
      
      builder.append ("</table>" +  LINE_SEP);
      return builder.toString();
   }   
}
