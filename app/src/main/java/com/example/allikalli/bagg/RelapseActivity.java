package com.example.allikalli.bagg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

public class RelapseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relapse);
    }

    public void tilMain(View v){
        finish();
        /*
        Intent goToMain = new Intent();
        goToMain.setClass(this, MainActivity.class);
        startActivity(goToMain);
        */
    }

    public void relapse(View v){
        //reset countdown progress

        //tilMain(v);
        SharedPreferences scores = getSharedPreferences("ScoreKeeper", Context.MODE_PRIVATE);
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        SharedPreferences.Editor scoreEdit = scores.edit();
        long nowInMillis = calendarTool.calToMillis(now);
        scoreEdit.putLong("DateofLastRelapse", nowInMillis);
        int timesRelapsed = scores.getInt("TimesRelapsed",-1)+1;
        scoreEdit.putInt("TimesRelapsed", timesRelapsed);
        scoreEdit.commit();
        //Toast.makeText(RelapseActivity.this, "DateOLastRelapse has been saved with value of: "+nowInMillis +" in ScoreKeeper", Toast.LENGTH_SHORT).show();
        Intent goToMainRelapsed = new Intent();
        goToMainRelapsed.setClass(this, MainActivity.class);
        goToMainRelapsed.putExtra("isRelapse", true);
        startActivity(goToMainRelapsed);
    }
}
