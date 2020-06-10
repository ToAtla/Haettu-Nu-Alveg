package com.example.allikalli.bagg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.TimeZone;

public class OpnunActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opnun);
       //getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences upplysingar = getSharedPreferences("Talning", Context.MODE_PRIVATE);

        if(upplysingar.contains("startCalendarValue")){
            beintTilMain();
        }
        //Meta hvort talning sé hafin
        //fara þá beint í main activity
/*
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        float milSeinna = ((float)calendarTool.calToMillis(now));
        Log.i("millis", ""+milSeinna);
        float milFyrra = milSeinna-15000;

        float mismunurMillis = milSeinna-milFyrra;

        float sec = mismunurMillis/1000;

        float min = sec/60;

        float hours = min/60;

        float days = hours/24;
        Log.i("Days difference", ""+days);
*/
    }

    public void beintTilMain(){

        Intent goToMain = new Intent();
        goToMain.setClass(this, MainActivity.class);
        startActivity(goToMain);
    }
    public void begin(View v){
        Intent goToInfo = new Intent();
        goToInfo.setClass(this, InfoActivity.class);
        startActivity(goToInfo);
    }
}
