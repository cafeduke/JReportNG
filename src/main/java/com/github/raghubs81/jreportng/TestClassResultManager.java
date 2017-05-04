package com.github.raghubs81.jreportng;

import java.time.*;
import java.util.*;

public class TestClassResultManager
{
   private static Map<Class<?>, TestClassResultManager> mapClassToResult = new Hashtable<> ();
   
   private Class<?> testClass;
   
   private LocalDateTime startTime, endTime;
   
   private Map<String,MethodInfo> mapMethodNameToInfo = new Hashtable<> ();
   
   private TestClassResultManager (Class<?> testClass)
   {
      this.testClass = testClass;
   }
   
   static TestClassResultManager getInstance (Class<?> testClass)
   {
      TestClassResultManager result = mapClassToResult.get(testClass);
      if (result == null)
      {
         result = new TestClassResultManager (testClass);
         mapClassToResult.put(testClass, result);
      }
      return result;
   }
   
   void startedTestClass ()
   {
      startTime = LocalDateTime.now();
   }   
   
   void finishedTestClass ()
   {
      if (startTime == null)
         throw new IllegalStateException ("startedClass() is not called, but finishedClass() is called. Class=" + testClass.getName());
      endTime = LocalDateTime.now();
   }
   
   String getTimeTaken ()
   {
      return formatTime(Duration.between(startTime, endTime).toMillis());
   }
   
   String getTimeTaken (String methodName)
   {
       return mapMethodNameToInfo.get(methodName).getTimeTaken();  
   }
   
   void startedTestMethod (String methodName)
   {
      MethodInfo methodInfo = mapMethodNameToInfo.get(methodName);
      if (methodInfo == null)
      {
         methodInfo = new MethodInfo (methodName);
         mapMethodNameToInfo.put(methodName, methodInfo);
      }
      methodInfo.startTime = LocalDateTime.now();
   }
   
   void finishedTestMethod (String methodName)
   {
      MethodInfo methodInfo = mapMethodNameToInfo.get(methodName);
      if (methodInfo == null)
         throw new IllegalStateException ("MethodInfo not found. startedMethod() not called, but finishedMethod() is called. Class=" + testClass.getName());
      methodInfo.endTime = LocalDateTime.now();      
   }
   
   void setTestMethodState (String methodName, MethodResultState state)
   {
      MethodInfo methodInfo = mapMethodNameToInfo.get(methodName);
      if (methodInfo == null)
         throw new IllegalStateException ("MethodInfo not found. startedMethod() not called, but setMethodState() is called. Class=" + testClass.getName());
      methodInfo.state = state;      
   }
   
   MethodResultState getTestMethodState (String methodName)
   {
      MethodInfo methodInfo = mapMethodNameToInfo.get(methodName);
      return (methodInfo == null) ? MethodResultState.UNKNOWN : methodInfo.state;
   }
   
   String getResultSummary ()
   {
      int passCount = 0, failCount = 0, skipCount = 0;
      for (MethodInfo info : mapMethodNameToInfo.values())
      {
         switch (info.state)
         {
            case PASSED : passCount++; break;            
            case FAILED : failCount++; break;             
            case SKIPPED: skipCount++; break;
            default: System.out.println("State not set as expected. State=" + info.state); break;
         }
      }
      int totalCount = mapMethodNameToInfo.size();
      return String.format("Total=%-2d, Pass=%-2d, Fail=%-2d, Skip=%-2d", totalCount, passCount, failCount, skipCount);
   }
   
   enum MethodResultState
   {
      UNKNOWN, PASSED, FAILED, SKIPPED;
   }
   
   private class MethodInfo
   {
      @SuppressWarnings("unused")
      protected String methodName;      
      protected LocalDateTime startTime, endTime;
      protected MethodResultState state = MethodResultState.UNKNOWN;
      
      public MethodInfo (String methodName)
      {
         this.methodName = methodName;
      }
      
      String getTimeTaken ()
      {
         assert !startTime.isAfter(endTime);
         return formatTime (Duration.between(startTime, endTime).toMillis());
      }
   }   

   static String formatTime (long milli)
   {
      long second = (milli / 1000) % 60;
      long minute = (milli / (1000 * 60)) % 60;
      long hour   = (milli / (1000 * 60 * 60));      
      milli = milli % 1000;
      
      return (hour == 0) ? String.format("%02d:%02d.%03d", minute, second, milli) :  String.format("%02d:%02d:%02d.%03d", hour, minute, second, milli);
   }   
}
