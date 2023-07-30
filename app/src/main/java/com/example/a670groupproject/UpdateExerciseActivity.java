package com.example.a670groupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;

public class UpdateExerciseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String tag = "UpdateExerciseActivity";
    EditText exerciseReading;
    EditText exerciseDay;
    EditText exerciseMonth;
    EditText exerciseYear;
    EditText exerciseHour;
    EditText exerciseMinute;
    String entryID;
    String entryValue;
    String entryHour;
    String entryMinute;
    String entryDay;
    String entryMonth;
    String entryYear;

    String amPMValue;

    private DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        entryID = intent.getStringExtra("entryID");
        entryValue = intent.getStringExtra("entryValue");
        entryHour = intent.getStringExtra("entryHour");
        entryMinute = intent.getStringExtra("entryMinute");
        entryDay = intent.getStringExtra("entryDay");
        entryMonth = intent.getStringExtra("entryMonth");
        entryYear = intent.getStringExtra("entryYear");
        amPMValue = intent.getStringExtra("amPM");

        Log.i(tag, "Entry ID is : "+entryID+" entry value is "+entryValue+" entry hour is "+entryHour+" entry minute is "+entryMinute);
        DB = new DBHelper(this);

        setContentView(R.layout.activity_update_exercise);
        Spinner spinner = (Spinner) findViewById(R.id.amPMSelector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.am_pm_spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        int spinnerPosition = adapter.getPosition(amPMValue);
        spinner.setSelection(spinnerPosition);

        exerciseDay = (EditText)findViewById(R.id.exerciseDay);
        exerciseMonth = (EditText)findViewById(R.id.exerciseMonth);
        exerciseYear = (EditText)findViewById(R.id.exerciseYear);
        exerciseReading = (EditText)findViewById(R.id.exerciseReading);
        exerciseHour = (EditText)findViewById(R.id.exerciseHour);
        exerciseMinute = (EditText)findViewById(R.id.exerciseMinute);
        exerciseReading.setText(entryValue);
        exerciseDay.setText(entryDay);
        exerciseMonth.setText(entryMonth);
        exerciseYear.setText(entryYear);
        exerciseHour.setText(entryHour);
        exerciseMinute.setText(entryMinute);

        Button updateExerciseButton = (Button) findViewById(R.id.updateexerciseButton);
        updateExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(tag, "Updating Entry ID: "+entryID);
                String value = exerciseReading.getText().toString();
                String day = exerciseDay.getText().toString();
                String month = exerciseMonth.getText().toString();
                String year = exerciseYear.getText().toString();
                String hour = exerciseHour.getText().toString();
                String minute = exerciseMinute.getText().toString();
                String amPMValue = spinner.getSelectedItem().toString();
                Boolean valid = inputValidationExercise(day, month, year, hour, minute, value);

                Log.i(tag, "Day: "+day+" month: "+month+" year: "+year);
                DB.updateEntry("exerciseTable", entryID, value, day, month, year, hour, minute, amPMValue);
                Log.i(tag, "Entry ID: "+entryID+" is updated");
                Intent startNewActivity = new Intent(getBaseContext(), MainActivity.class);
                startActivityForResult(startNewActivity,10);

            }
        });

        Button deleteExerciseButton = (Button) findViewById(R.id.deleteexerciseButton);
        deleteExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(tag, "Deleting Entry ID: "+entryID);
                DB.deleteEntry("exerciseTable", entryID);
                Log.i(tag, "Entry ID: "+entryID+" is deleted");
                Intent startNewActivity = new Intent(getBaseContext(), MainActivity.class);
                startActivityForResult(startNewActivity,10);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        amPMValue = (String) adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        amPMValue = "AM";
    }

    public boolean inputValidationExercise(String day, String month, String year, String hour, String minute, String value)
    {
        Boolean valid = true;
        DecimalFormat df = new DecimalFormat();

        if (Integer.parseInt(day)>31 ||Integer.parseInt(day)<1)
        {
            Log.i(tag, "Invalid day in update exercise activity");
            valid=false;
        }
        if (Integer.parseInt(month)>12 ||Integer.parseInt(month)<1)
        {
            Log.i(tag, "Invalid month in update exercise activity");
            valid = false;
        }
        if (Integer.parseInt(hour)>12 ||Integer.parseInt(hour)<0)
        {
            Log.i(tag, "Invalid hour in update exercise activity");
            valid = false;
        }
        if (Integer.parseInt(minute)>59 ||Integer.parseInt(minute)<0)
        {
            Log.i(tag, "Invalid minute in update exercise activity");
            valid = false;
        }
        if (month.length() !=2 || day.length() !=2 || hour.length() !=2|| minute.length() !=2 || year.length() !=4)
        {
            Log.i(tag, "Invalid formatting of day/month/year in update exercise activity");
            valid = false;
        }
        return valid;
    }
}