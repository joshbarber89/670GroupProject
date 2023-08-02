package com.example.a670groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    RadioButton bloodSugarLevelUnit1;  //  mmol/L

    public static int BloodSugarUnit_mmolPerLitre = 0;

    RadioButton bloodSugarLevelUnit2; // mg/dL

    public static int BloodSugarUnit_mgPerDeciLitre = 1;

    RadioButton bloodInsulinLevelUnit1; // IU/mL

    public static int BloodInsulinUnit_uIUPerMilliLitre = 0;

    RadioButton bloodInsulinLevelUnit2; // pmol/L

    public static int BloodInsulinUnit_pmolPerLitre = 1;

    Button musicButton;     // play or not play relaxing music.

    private static String tag = "SettingsActivity";

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bloodSugarLevelUnit1 = findViewById(R.id.bloodSugarRadio1);
        bloodSugarLevelUnit2 = findViewById(R.id.bloodSugarRadio2);
        bloodInsulinLevelUnit1 = findViewById(R.id.insulinRadio1);
        bloodInsulinLevelUnit2 = findViewById(R.id.insulinRadio2);
        musicButton = findViewById(R.id.musicButton);

        mp = MediaPlayer.create(this, R.raw.music);

        if (MainActivity.specificUserPrefs.getInt("bloodSugarUnits", BloodSugarUnit_mmolPerLitre) == BloodSugarUnit_mmolPerLitre)
            bloodSugarLevelUnit1.setChecked(true);

        if (MainActivity.specificUserPrefs.getInt("bloodSugarUnits", BloodSugarUnit_mmolPerLitre) == BloodSugarUnit_mgPerDeciLitre)
            bloodSugarLevelUnit2.setChecked(true);

        if (MainActivity.specificUserPrefs.getInt("bloodInsulinUnits", BloodInsulinUnit_uIUPerMilliLitre) == BloodInsulinUnit_uIUPerMilliLitre)
            bloodInsulinLevelUnit1.setChecked(true);

        if (MainActivity.specificUserPrefs.getInt("bloodInsulinUnits", BloodInsulinUnit_uIUPerMilliLitre) == BloodInsulinUnit_pmolPerLitre)
            bloodInsulinLevelUnit2.setChecked(true);

        for(MediaPlayer mediaPlayer : ActiveMedia.mediaPlayers) {
            if (mediaPlayer.isPlaying()) {
                musicButton.setText(R.string.stopRelaxingMusic);
            }
        }

        bloodSugarLevelUnit1.setOnClickListener(this);
        bloodSugarLevelUnit2.setOnClickListener(this);
        bloodInsulinLevelUnit1.setOnClickListener(this);
        bloodInsulinLevelUnit2.setOnClickListener(this);
        musicButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        SharedPreferences.Editor myEditor = MainActivity.specificUserPrefs.edit();

        if (v.getId() == R.id.bloodSugarRadio1){

            myEditor.putInt("bloodSugarUnits", BloodSugarUnit_mmolPerLitre);
            MainActivity.selectedBloodSugarUnit = BloodSugarUnit_mmolPerLitre;

        }
        else if (v.getId() == R.id.bloodSugarRadio2){

            myEditor.putInt("bloodSugarUnits", BloodSugarUnit_mgPerDeciLitre);
            MainActivity.selectedBloodSugarUnit = BloodSugarUnit_mgPerDeciLitre;

        }
        else if (v.getId() == R.id.insulinRadio1){

            myEditor.putInt("bloodInsulinUnits", BloodInsulinUnit_uIUPerMilliLitre);
            MainActivity.selectedBloodInsulinUnit = BloodInsulinUnit_uIUPerMilliLitre;

        }
        else if (v.getId() == R.id.insulinRadio2){

            myEditor.putInt("bloodInsulinUnits", BloodInsulinUnit_pmolPerLitre);
            MainActivity.selectedBloodInsulinUnit = BloodInsulinUnit_pmolPerLitre;

        }
        else if (v.getId() == R.id.musicButton){

            boolean isplaying = false;

            for(MediaPlayer mediaPlayer : ActiveMedia.mediaPlayers){

                if(mediaPlayer.isPlaying()){
                    //there is an active media player
                    isplaying = true;
                    mediaPlayer.pause();

                    ActiveMedia.mediaPlayers.remove(mediaPlayer);
                    musicButton.setText(R.string.playRelaxingMusic);
                }

            }

            if(!isplaying){
                mp.start();
                ActiveMedia.mediaPlayers.add(mp);
                musicButton.setText(R.string.stopRelaxingMusic);
            }

        }

        myEditor.apply();

    }



}