package com.example.a670groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    RadioButton bloodSugarLevelUnit1;  //  mmol/L

    public static int BloodSugarUnit_mmolPerLitre = 0;

    RadioButton bloodSugarLevelUnit2; // mg/dL

    public static int BloodSugarUnit_mgPerDeciLitre = 1;

    RadioButton bloodInsulinLevelUnit1; // IU/mL

    public static int BloodInsulinUnit_IUPerMilliLitre = 0;

    RadioButton bloodInsulinLevelUnit2; // pmol/L

    public static int BloodInsulinUnit_pmolPerLitre = 1;

    Switch musicSwitch;     // play or not play relaxing music.

    private static String tag = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bloodSugarLevelUnit1 = findViewById(R.id.bloodSugarRadio1);
        bloodSugarLevelUnit2 = findViewById(R.id.bloodSugarRadio2);
        bloodInsulinLevelUnit1 = findViewById(R.id.insulinRadio1);
        bloodInsulinLevelUnit2 = findViewById(R.id.insulinRadio2);
        musicSwitch = findViewById(R.id.musicSwitch);

        if (MainActivity.specificUserPrefs.getInt("bloodSugarUnits", BloodSugarUnit_mmolPerLitre) == BloodSugarUnit_mmolPerLitre)
            bloodSugarLevelUnit1.setChecked(true);

        if (MainActivity.specificUserPrefs.getInt("bloodSugarUnits", BloodSugarUnit_mmolPerLitre) == BloodSugarUnit_mgPerDeciLitre)
            bloodSugarLevelUnit2.setChecked(true);

        if (MainActivity.specificUserPrefs.getInt("bloodInsulinUnits", BloodInsulinUnit_IUPerMilliLitre) == BloodInsulinUnit_IUPerMilliLitre)
            bloodInsulinLevelUnit1.setChecked(true);

        if (MainActivity.specificUserPrefs.getInt("bloodInsulinUnits", BloodInsulinUnit_IUPerMilliLitre) == BloodInsulinUnit_pmolPerLitre)
            bloodInsulinLevelUnit2.setChecked(true);

        if (MainActivity.specificUserPrefs.getInt("playMusic", 0) == 0)
            musicSwitch.setChecked(false);
        else{
            musicSwitch.setChecked(true);
        }

        bloodSugarLevelUnit1.setOnClickListener(this);
        bloodSugarLevelUnit2.setOnClickListener(this);
        bloodInsulinLevelUnit1.setOnClickListener(this);
        bloodInsulinLevelUnit2.setOnClickListener(this);
        musicSwitch.setOnClickListener(this);

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

            myEditor.putInt("bloodInsulinUnits", BloodInsulinUnit_IUPerMilliLitre);
            MainActivity.selectedBloodInsulinUnit = BloodInsulinUnit_IUPerMilliLitre;

        }
        else if (v.getId() == R.id.insulinRadio2){

            myEditor.putInt("bloodInsulinUnits", BloodInsulinUnit_pmolPerLitre);
            MainActivity.selectedBloodInsulinUnit = BloodInsulinUnit_pmolPerLitre;

        }
        else if (v.getId() == R.id.musicSwitch){

            String state = musicSwitch.getStateDescription().toString();
            Log.i(tag, "Turning " + state + " music!");

            if (state.equals("ON")){
                myEditor.putInt("playMusic", 1);
                MainActivity.playMusic = 1;
            }
            else{
                myEditor.putInt("playMusic", 0);
                MainActivity.playMusic = 0;
            }

        }

        myEditor.apply();

    }



}