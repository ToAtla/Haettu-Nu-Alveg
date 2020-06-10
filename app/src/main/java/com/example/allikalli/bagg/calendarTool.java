package com.example.allikalli.bagg;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;
/**
 * Created by Allikalli on 23-Sep-16.
 */
public class calendarTool
{

    public static long fakeTestTime;

    public static long getFakeTestTime(){

        return 0;
    }

    public static long setFakeTestTime(){

        return 0;
    }

/*
    public static int dagaMunur(long fyrriDagur, long seinniDagur){
        long milliMismunur = seinniDagur-fyrriDagur;
        long millisec = milliMismunur/1000;
        int dagamunur = (int) millisec/3600;
        return dagamunur;
    }
    */
    public static long calToMillis(Calendar inputCal){
        //Returns a long value of the milliseconds passed since
        // 00:00:00 21. September 2016
        // i.e. 2016 September 21 00:00:00
        // 265. dagur ársins


        int seconds = inputCal.get(Calendar.SECOND);
        int minutes = inputCal.get(Calendar.MINUTE);
        int hours = inputCal.get(Calendar.HOUR_OF_DAY);
        int dayOfYear = inputCal.get(Calendar.DAY_OF_YEAR);
        int year = inputCal.get(Calendar.YEAR);


        //Log.d("Seconds", ""+seconds+" -------------------------");

 //       Log.d("Minutes", ""+minutes+" -------------------------");

   //     Log.d("Hours", ""+hours+" -------------------------");

     //   Log.d("Day of Year", ""+dayOfYear+" -------------------------");

       // Log.d("Year", ""+year+" -------------------------");


        int misYears = year-2016;
        int misDays;
        int misHours;
        int misMinutes;
        int misSeconds;

        // er ár það sama?
           if(misYears == 0){
               //sama ár
               misDays = dayOfYear-265;
           }else{
               //dagamunur bætist við
               misDays = 365-265+365*(misYears-1)+dayOfYear;
           }
        // er dagur árs sami?
            misHours = misDays*24+hours;
            misMinutes = misHours*60 +minutes;
            misSeconds = misMinutes*60 + seconds;

        long millur = misSeconds*1000;
        //er klukkutími samur?

        //er mínúta söm?

        //er sekúnda söm?
        return millur;
    }
}
