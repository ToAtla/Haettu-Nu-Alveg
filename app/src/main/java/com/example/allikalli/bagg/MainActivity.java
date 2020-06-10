package com.example.allikalli.bagg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();
    int nrLummu;
    int amountOfFives;
    CountDownTimer talning;
    boolean relapse;
    boolean haveble;
    double klstMilliLumma;
    float upphaflegarKlukkustundirMilliLumma;
    float yearBuff;
    TextView uppteljari;

    public void retriever(){
        SharedPreferences infoPref = getSharedPreferences("InfoFromUser",Context.MODE_PRIVATE);
        nrLummu = infoPref.getInt("NrLummu",-1);
        upphaflegarKlukkustundirMilliLumma = infoPref.getFloat("UpphaflegarKlukkustundirMilliLumma",-1);
        yearBuff = infoPref.getFloat("YearBuffConstant", -1);
        haveble = false;
    }

    public double fall(){
        return (upphaflegarKlukkustundirMilliLumma*(Math.pow(1.05,nrLummu*yearBuff)));
    }

    public boolean geturStoppad(double klstUntilNextHit){
        if(klstUntilNextHit > 24 && klstUntilNextHit/klstMilliLumma > 5){
            return true;
        }
        return false;
    }

    public long reiknari(){
        double klstUntilNextHit = fall();
        double millisAMilli = klstUntilNextHit*60*60*1000;
        long langarMillisMilli = (long) millisAMilli;
        //Getur notandinn hætt?
        if(geturStoppad(klstUntilNextHit)){
            //Bjóða honum að hætta
        }
        return langarMillisMilli;
        //((TextView) findViewById(R.id.textCountdownMain)).setText("");

        //birtari(langarMillisMilli);
        //return 10000;

    }







    /*

    public void birtari(long millisStatic){
        TextView clocker =((TextView) findViewById(R.id.textCountdownMain));
        TextView nrLum = ((TextView) findViewById(R.id.debugLNR));
        nrLum.setText("L.Nr. "+nrLummu);
        int seconds;
        int minutes;
        int hours;
        seconds = (int) millisStatic/1000;
        minutes = seconds/60;
        seconds = seconds%60;
        hours   = minutes/60;
        minutes = minutes%60;

        String sMinutes = ""+minutes;
        String sSeconds = ""+seconds;
        String sHours = ""+hours;
        if(minutes<10){
            sMinutes = "0"+sMinutes;
        }
        if(seconds<10){
            sSeconds = "0"+sSeconds;
        }
        if(hours<10){
            sHours = "0"+sHours;
        }
        clocker.setText(sHours +":"+sMinutes+":"+sSeconds);

    }

    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retriever();
        Intent caller = getIntent();
        relapse = caller.getBooleanExtra("isRelapse",false);
        SharedPreferences pruf = getSharedPreferences("Talning",Context.MODE_PRIVATE);
        if (!pruf.contains("startCalendarValue")) {
            telja(reiknari());
        } else if (relapse) {
            Toast.makeText(MainActivity.this, R.string.toastRelapseMainScreen, Toast.LENGTH_SHORT).show();
            telja(reiknari());
        } else {
            //halda áfram með miðja talningu
            //eða ganga beint inn í upptalningu
            inToMiddle(meta());
        }
    }

    public long meta(){
        SharedPreferences preffur = getSharedPreferences("Talning",Context.MODE_PRIVATE);
        long startCalMillis = preffur.getLong("startCalendarValue", -1);
        Calendar later = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        long laterCalMillis = calendarTool.calToMillis(later);
        long result = laterCalMillis-startCalMillis;
        long countDownMillis = preffur.getLong("MillisValueOfCountdown", -1);
        return countDownMillis - result;
    }

    public void inToMiddle(long mismunurMilli){
        if(mismunurMilli > 0){
            telja(mismunurMilli);
        }else{
            whenTimerZero();
        }
    }

    public void upptalning(){
        uppteljari = ((TextView) findViewById(R.id.textUpptalningMain));
        uppteljari.setVisibility(View.VISIBLE);
        handler.postDelayed(hendaIupptalningu,0);

    }

    public void telja(long millis){

        Calendar beforeCountdown = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        long millisOfCal = calendarTool.calToMillis(beforeCountdown);

        SharedPreferences upplysingar = getSharedPreferences("Talning", Context.MODE_PRIVATE);
        SharedPreferences.Editor upplEditor = upplysingar.edit();
        upplEditor.putLong("startCalendarValue", millisOfCal);
        upplEditor.putLong("MillisValueOfCountdown", millis);
        upplEditor.commit();



        talning = new CountDownTimer(millis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TextView clocker = (TextView) findViewById(R.id.textCountdownMain);
                //TextView clocker =((TextView) findViewById(R.id.debugCountdown));
                int seconds;
                int minutes;
                int hours;
                seconds = (int) millisUntilFinished/1000;
                minutes = seconds/60;
                seconds = seconds%60;
                hours   = minutes/60;
                minutes = minutes%60;

                String sMinutes = ""+minutes;
                String sSeconds = ""+seconds;
                String sHours = ""+hours;
                if(minutes<10){
                    sMinutes = "0"+sMinutes;
                }
                if(seconds<10){
                    sSeconds = "0"+sSeconds;
                }
                if(hours<10){
                    sHours = "0"+sHours;
                }
                clocker.setText(sHours +":"+sMinutes+":"+sSeconds);
            }

            @Override
            public void onFinish() {
                whenTimerZero();
            }
        }.start();
    }

    public void whenTimerZero(){
        Button hadSome = (Button) findViewById(R.id.buttonMainScreenHadSome);
        hadSome.setBackgroundColor(Color.GREEN);
        ((TextView) findViewById(R.id.textCountdownMain)).setText("00:00:00");
        haveble = true;
        upptalning();
        (findViewById(R.id.imageRelapseFrownMain)).setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

    }

    public Runnable hendaIupptalningu = new Runnable() {
        @Override
        public void run() {
            long timeToClock = meta()*(-1);

            int secs = (int) (timeToClock / 1000);
            int mins = secs / 60;
            int hours = mins/60;
            secs = secs % 60;
            mins = mins&60;

            String sMinutes = ""+mins;
            String sSeconds = ""+secs;
            String sHours = ""+hours;
            if(mins<10){
                sMinutes = "0"+sMinutes;
            }
            if(secs<10){
                sSeconds = "0"+sSeconds;
            }
            if(hours<10){
                sHours = "0"+sHours;
            }

            TextView time = (TextView) findViewById(R.id.textUpptalningMain);
            time.setText("+ "+sHours +":"+sMinutes+":"+sSeconds);
            handler.postDelayed(this, 0);

        }
    };


    public void haveSome(View v){

        //toast
        if(haveble) {
            uppteljari = ((TextView) findViewById(R.id.textUpptalningMain));
            uppteljari.setVisibility(View.INVISIBLE);

            SharedPreferences info = getSharedPreferences("InfoFromUser",Context.MODE_PRIVATE);
            nrLummu = nrLummu+1;
            SharedPreferences.Editor editz = info.edit();
            editz.putInt("NrLummu", nrLummu);
            editz.commit();
            telja(reiknari());
            Button takkinn = (Button) findViewById(R.id.buttonMainScreenHadSome);
            takkinn.setBackgroundColor(Color.DKGRAY);
            haveble = false;

            SharedPreferences score = getSharedPreferences("ScoreKeeper", Context.MODE_PRIVATE);
            SharedPreferences.Editor scoreEdit = score.edit();
            int nrOfFives = score.getInt("NumberOfFives",-1)+amountOfFives;
            scoreEdit.putInt("NumberOfFives",nrOfFives);
            scoreEdit.commit();
            handler.removeCallbacks(hendaIupptalningu);
            (findViewById(R.id.imageRelapseFrownMain)).setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(MainActivity.this, R.string.toastMainScreenWait, Toast.LENGTH_SHORT).show();

        }
    }

    public void tilScore(View v){
        Intent goToScore = new Intent();
        goToScore.setClass(this, ScoreActivity.class);

        startActivity(goToScore);

    }

    public void tilRelapse(View v){
        Intent goToRelapse = new Intent();
        goToRelapse.setClass(this, RelapseActivity.class);

        startActivity(goToRelapse);

    }
}
