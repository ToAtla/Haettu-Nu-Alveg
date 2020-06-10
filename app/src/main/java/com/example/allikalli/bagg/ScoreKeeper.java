package com.example.allikalli.bagg;

import android.app.Application;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Allikalli on 24-Sep-16.
 */
public class ScoreKeeper extends Application {

    private static int score;
    private static int hitsAvoided;
    private static double moneySaved;
    private static int daysInARow;

    public ScoreKeeper(){



        //Stig - Score
        //Hver dagur gefur 25 stig
        //Hverjir 3 dagar í röð gefa önnur 30
        //Hverjar fimm mínútur framyfir klukku gefa 1 stig


        /*
        daysInARow = prefs.getInt("DaysInARow",-1);
        int fimmurFramyfir = prefs.getInt("FimmurFramyfir",0);
        score = daysInARow*10+ fimmurFramyfir*1 + hitsAvoided*2;

        */

    }

    public static int getScore(){
        return score;
    }
    public static int getHitsAvoided(){
        return hitsAvoided;
    }
    public static double getMoneySaved(){
        return moneySaved;
    }

}
