package com.example.allikalli.bagg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.TimeZone;

public class ScoreActivity extends AppCompatActivity {

    int hitsAvoided;
    int score;
    float moneySaved;
    int daysInARow;
    float daysFromStart;
    int progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        //ScoreKeeper stigin = new ScoreKeeper(getApplicationContext());
        //calculate Scores
        calculate();

        //get stored values
        //getScoreValues();

        //int
        //Um er að ræða

        ((TextView) findViewById(R.id.textNumHitsAvoided)).setText(""+hitsAvoided);
        ((TextView) findViewById(R.id.textNumMoneySaved)).setText(""+moneySaved);
        ((TextView) findViewById(R.id.textNumScoreScoreScreen)).setText(""+score);
        ((ProgressBar) findViewById(R.id.progressBar)).setProgress(progress);
        ((TextView) findViewById(R.id.textProgressPercentage)).setText(""+progress +" %");


        //Lummur forðast
            //lummur sem hefðu verið teknar - lummur teknar

        //Peningar Sparaðir
            //(Dollur sem hefðu verið kláraðar - mínus dollur kláraðar)*kostnaður dollu

        //Dagar sinntir
            //dagar frá síðasta relapse-i eða upphafi


        //display them
        //modifyValues();
    }

    public void calculate(){
        SharedPreferences prefs = getSharedPreferences("ScoreKeeper", Context.MODE_PRIVATE);
        SharedPreferences info = getSharedPreferences("InfoFromUser", Context.MODE_PRIVATE);

        SharedPreferences upplysingar = getSharedPreferences("Talning", Context.MODE_PRIVATE);
        long thenInMillis =upplysingar.getLong("startCalendarValue",-1);
        if (thenInMillis ==-1)
            Toast.makeText(ScoreActivity.this, "startCalendarValue var -1!", Toast.LENGTH_SHORT).show();
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        long nowInMillis = calendarTool.calToMillis(now);
        float mismunurFraUpphafi = nowInMillis-thenInMillis;
        daysFromStart = mismunurFraUpphafi/86400000;
        //Toast.makeText(ScoreActivity.this, "Days from start="+daysFromStart, Toast.LENGTH_SHORT).show();

        //calculate money saved
        int orgLummurPerDag = info.getInt("LummurPerDag", -1);
        if (orgLummurPerDag ==-1)
            Toast.makeText(ScoreActivity.this, "orgLummurPerdag var -1!", Toast.LENGTH_SHORT).show();
        int orgLummurTeknar = ((int) (orgLummurPerDag*daysFromStart));
        //Toast.makeText(ScoreActivity.this, "vegna ekki daysinarow"+orgLummurTeknar, Toast.LENGTH_SHORT).show();
        int realLummurTeknar = info.getInt("NrLummu", -1);
        if (realLummurTeknar ==-1)
            Toast.makeText(ScoreActivity.this, "realLummurTeknar var -1!", Toast.LENGTH_SHORT).show();
        int lummuMunur = orgLummurTeknar-realLummurTeknar;
        if(!(lummuMunur <0)){
            hitsAvoided = orgLummurTeknar-realLummurTeknar;

        }else{
            hitsAvoided=0;
        }
        //Toast.makeText(ScoreActivity.this, "OrgLummur "+ orgLummurTeknar + " og realLummur "+ realLummurTeknar + " og hitsAvoid "+ hitsAvoided, Toast.LENGTH_SHORT).show();

        float notendaLummurIDollu = info.getFloat("NotendaLummurIDollu",-1);
        if (notendaLummurIDollu ==-1)
            Toast.makeText(ScoreActivity.this, "notendaLummurIDollu var -1!", Toast.LENGTH_SHORT).show();
        int verd = info.getInt("PriceOfCan", -1);
        if (verd ==-1)
            Toast.makeText(ScoreActivity.this, "verd var -1!", Toast.LENGTH_SHORT).show();
        float kostnadurLummu = ((float) verd)/notendaLummurIDollu;
        moneySaved = hitsAvoided*kostnadurLummu;



        long lastRelapseInMillis = prefs.getLong("DateofLastRelapse", -1);
        Log.i("lastRelapseInMillis", ""+lastRelapseInMillis);

        Log.i("nowInMillis", ""+nowInMillis);
        float mismunur = nowInMillis-lastRelapseInMillis;
        Log.i("DaysMismunurInMillis", ""+mismunur);

        int displayMismunur = ((int) mismunur/86400000);

        ((TextView) findViewById(R.id.textNumCurrentStreak)).setText(""+displayMismunur+getString(R.string.textDaysStreakScore));
        Log.i("TheSameButInDays", "Value/3600 is "+ displayMismunur);


        //Stig - Score
        //Hver Lumma gefur 1 stig(nema eftir relapse)
        //Hverjir 3 dagar í röð gefa 5 stig
        //Hverjar fimm mínútur framyfir klukku gefa 1 stig


        int timesRelased = prefs.getInt("TimesRelapsed", -1);
        int successfulLummur = realLummurTeknar-timesRelased;
        int numberofFives = prefs.getInt("NumberOfFives",-1);
        int scoreAddition = displayMismunur/3;
        int orgScore = prefs.getInt("Score",-1);
        int newScore = orgScore+scoreAddition;
        score = orgScore+scoreAddition+numberofFives+successfulLummur;
        SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.putInt("Score",newScore);

        //calculate progress

        //(A/B)*(1/ln(1,05))*1,05^(xB)
        float A = prefs.getFloat("UpphaflegarKlukkustundirMilliLumma",-1);
        float B = prefs.getFloat("YearBuffConstant",-1);
        float value = (A/B)*((float)((Math.pow(1.05,24*B))/(Math.log(1.05))));
        float minusDude = (A/B)*((float) (1/(Math.log(1.05))));
        float klstToFinish = value-minusDude;
        float millis = klstToFinish*60*60*1000;

        progress = ((int)(100*(mismunurFraUpphafi)/millis));
        //Toast.makeText(ScoreActivity.this, "Progress is "+progress, Toast.LENGTH_SHORT).show();


    }


    public void tilMain(View v){
        finish();
        /*
        Intent goToMain = new Intent();
        goToMain.setClass(this, MainActivity.class);
        startActivity(goToMain);
        */
    }

    public void reset(View v){
        //Here a warning is neccessary

        SharedPreferences prefs = getSharedPreferences("Talning", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

        SharedPreferences prefs2 = getSharedPreferences("ScoreKeeper", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = prefs2.edit();
        editor2.clear();
        editor2.commit();

        SharedPreferences prefs3 = getSharedPreferences("InfoFromUser",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor3 = prefs3.edit();
        editor3.clear();
        editor3.commit();


        Intent goToOpening = new Intent();
        goToOpening.setClass(this, OpnunActivity.class);
        startActivity(goToOpening);

    }
}
