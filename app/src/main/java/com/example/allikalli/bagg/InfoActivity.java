package com.example.allikalli.bagg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class InfoActivity extends AppCompatActivity {

    TextView textHitsPerDay;
    TextView textDaysPerCan;
    int hits = 1;//Upphafsgildin í layoutinu
    int days = 14;//Upphafsgildin í layoutinu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        SeekBar barHitsPerDay = (SeekBar) findViewById(R.id.seekBarHitsPerDay);
        barHitsPerDay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                progress = progress/5;
                hits = progress;
                //Update value
                textHitsPerDay = (TextView) findViewById(R.id.textViewHitsPerDay);
                textHitsPerDay.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar barDaysPerCan = (SeekBar) findViewById(R.id.seekBarDaysPerCan);
        barDaysPerCan.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)  {

                progress = progress/3;
                progress = progress*2;
                days = progress;
                textDaysPerCan = (TextView) findViewById(R.id.textViewDaysPerCan);
                textDaysPerCan.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public float yearBuff(int years){

        //For more than 2 years
        //*1
        //For 2 years
        //*1.3
        //For 1 years
        //*1.5
        //For 0 years
        //*1.8
        if (years ==0){
            double a = 1.8;
            float b = (float) a;
            return b;
        }else if (years == 1){
            double a = 1.5;
            float b = (float) a;
            return b;
        }else if(years == 2){
            double a = 1.3;
            float b = (float) a;
            return b;
        }else{
            return 1;
        }

    }

    public void enter(View v){

        Intent goToMain = new Intent();
        goToMain.setClass(this, MainActivity.class);

        float upphaflegarKlukkustundirMilliLumma = ((float) 16)/hits;
        //Log.i("InfoActivity", "Útreiknaðar í klukkustundir á milli: "+ upphaflegarKlukkustundirMilliLumma + "  en hits var: "+ hits);
        float notendaLummurIDollu = ((float)(days*24))/upphaflegarKlukkustundirMilliLumma;

        EditText yearsInput = (EditText) findViewById(R.id.editText);
        String sYears = yearsInput.getText().toString();
        int years;
        if(sYears.equals("")){
            years =0;
        }else{
            years = Integer.parseInt(sYears);
        }
        float yearBuff = yearBuff(years);

        EditText priceInput = (EditText) findViewById(R.id.editTextPriceOfCan);
        String sPrice = priceInput.getText().toString();
        int price;
        if(sPrice.equals("")){
            price =0;
        }else{
            price = Integer.parseInt(sPrice);
        }

        if(years>=0 && price>=0){
            SharedPreferences infoPref = getSharedPreferences("InfoFromUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = infoPref.edit();
            editor.putInt("LummurPerDag", hits);
            //editor.putInt("DagarPerDolla", days);
            //editor.putInt("YearsActive", years);
            editor.putInt("PriceOfCan",price);
            editor.putFloat("UpphaflegarKlukkustundirMilliLumma",upphaflegarKlukkustundirMilliLumma);
            editor.putFloat("NotendaLummurIDollu",notendaLummurIDollu);
            editor.putFloat("YearBuffConstant",yearBuff);
            editor.putInt("NrLummu", 1);
            editor.commit();

            SharedPreferences scores = getSharedPreferences("ScoreKeeper", Context.MODE_PRIVATE);
            Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            SharedPreferences.Editor scoreEdit = scores.edit();
            long nowInMillis = calendarTool.calToMillis(now);
            //Toast.makeText(InfoActivity.this, "nowInMillis="+nowInMillis, Toast.LENGTH_SHORT).show();
            scoreEdit.putLong("DateofLastRelapse", nowInMillis);
            scoreEdit.putInt("TimesRelapsed",0);
            scoreEdit.putInt("Score",0);
            scoreEdit.commit();


            startActivity(goToMain);
        }else{
            Toast.makeText(InfoActivity.this, R.string.toastNegativeValuesInfoScreen, Toast.LENGTH_SHORT).show();
        }
    }
}
