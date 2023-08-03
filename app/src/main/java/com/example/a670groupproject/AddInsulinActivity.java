package com.example.a670groupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class AddInsulinHelper {

    private static final String tag = "AddInsulinActivity";
    private static final String TABLE1 = "insulinTable0";
    private static final String TABLE2 = "insulinTable1";
    protected DBHelper DB;

    public boolean insert(String day, String month, String year, String hour, String minute, String value, String amPMValue) {
        if (this.inputValidation(day, month, year, hour, minute, value)) {

            if (MainActivity.selectedBloodSugarUnit == SettingsActivity.BloodSugarUnit_mmolPerLitre) {
                DB.insertEntry(TABLE1, value,day,month,year,hour,minute,amPMValue);
                DB.insertEntry(TABLE2,(Float.parseFloat(value) * 6) + "",day,month,year,hour,minute,amPMValue);

            } else {
                DB.insertEntry(TABLE2,value, day,month,year,hour,minute,amPMValue);
                DB.insertEntry(TABLE1,(Float.parseFloat(value) / 6) + "",day,month,year,hour,minute,amPMValue);

            }
            Log.i(tag, "Insulin Entry Loaded into Database");
            return true;
        }
        return false;
    }
    private boolean inputValidation(String day, String month, String year, String hour, String minute, String value)
    {
        boolean valid = true;
        DecimalFormat df = new DecimalFormat();

        if (Integer.parseInt(day)>31 ||Integer.parseInt(day)<1)
        {
            Log.i(tag, "Invalid day in update blood sugar activity");
            valid=false;
        }
        if (Integer.parseInt(month)>12 ||Integer.parseInt(month)<1)
        {
            Log.i(tag, "Invalid month in update blood sugar activity");
            valid = false;
        }
        if (Integer.parseInt(hour)>12 ||Integer.parseInt(hour)<0)
        {
            Log.i(tag, "Invalid hour in update blood sugar activity");
            valid = false;
        }
        if (Integer.parseInt(minute)>59 ||Integer.parseInt(minute)<0)
        {
            Log.i(tag, "Invalid minute in update blood sugar activity");
            valid = false;
        }
        try {
            df.parse(value).floatValue();
        } catch (ParseException e)
        {
            Log.i(tag, "Invalid formatting of value in update blood sugar activity");
            valid = false;
        }
        return valid;
    }

    public String datetimeModification(int value) {
        if (value<10) {
            return "0"+value;
        }
        return String.valueOf(value);
    }
}

public class AddInsulinActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String tag = "AddInsulinActivity";
    EditText insulinReading;
    EditText insulinDay;
    EditText insulinMonth;
    EditText insulinYear;
    EditText insulinHour;
    EditText insulinMinute;
    String amPMValue;

    AddInsulinHelper aih = new AddInsulinHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Calendar cal = Calendar.getInstance();
        cal.getTime();
        int yearInt = cal.get(Calendar.YEAR);
        int monthInt = cal.get(Calendar.MONTH);
        int dayInt = cal.get(Calendar.DAY_OF_MONTH);
        int hourInt = cal.get(Calendar.HOUR);
        int minuteInt = cal.get(Calendar.MINUTE);

        aih.DB = new DBHelper(this);
        setContentView(R.layout.activity_add_insulin);
        Spinner spinner = (Spinner) findViewById(R.id.amPMSelector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.am_pm_spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Button submitInsulinButton = (Button) findViewById(R.id.submitinsulinButton);

        insulinDay = (EditText)findViewById(R.id.insulinDay);
        insulinDay.setText(aih.datetimeModification(dayInt));
        insulinMonth = (EditText)findViewById(R.id.insulinMonth);
        insulinMonth.setText(aih.datetimeModification(monthInt));
        insulinYear = (EditText)findViewById(R.id.insulinYear);
        insulinYear.setText(String.valueOf(yearInt));
        insulinHour = (EditText)findViewById(R.id.insulinHour);
        insulinHour.setText(aih.datetimeModification(hourInt));
        insulinMinute = (EditText)findViewById(R.id.insulinMinute);
        insulinMinute.setText(aih.datetimeModification(minuteInt));
        submitInsulinButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               insulinReading = (EditText)findViewById(R.id.insulinReading);
               insulinDay = (EditText)findViewById(R.id.insulinDay);
               insulinMonth = (EditText)findViewById(R.id.insulinMonth);
               insulinYear = (EditText)findViewById(R.id.insulinYear);
               insulinHour = (EditText)findViewById(R.id.insulinHour);
               insulinMinute = (EditText)findViewById(R.id.insulinMinute);
               Spinner spinner = (Spinner) findViewById(R.id.amPMSelector);
               String value = insulinReading.getText().toString();
               String day = insulinDay.getText().toString();
               String month = insulinMonth.getText().toString();
               String year = insulinYear.getText().toString();
               String hour = insulinHour.getText().toString();
               String minute = insulinMinute.getText().toString();
               String amPMValue = spinner.getSelectedItem().toString();
               boolean inputValid = aih.insert(day, month, year, hour, minute, value, amPMValue);
               if (inputValid) {
                   Intent startNewActivity = new Intent(getBaseContext(), MainActivity.class);
                   startActivityForResult(startNewActivity,10);
               }
               else
               {
                   Log.i(tag, "Invalid input in add insulin activity");
                   AlertDialog.Builder customDialog = new AlertDialog.Builder(AddInsulinActivity.this);
                   LayoutInflater inflater = AddInsulinActivity.this.getLayoutInflater();
                   final View view = inflater.inflate(R.layout.custom_dialog, null);
                   customDialog.setView(view)
                           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int id) {
                               }
                           });
                   Dialog dialog = customDialog.create();
                   dialog.show();
               }

           }
        }
        );

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        amPMValue = (String) adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        amPMValue = "AM";
    }
}