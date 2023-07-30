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

public class AddExerciseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String tag = "AddExerciseActivity";
    EditText exerciseReading;
    EditText exerciseDay;
    EditText exerciseMonth;
    EditText exerciseYear;
    EditText exerciseHour;
    EditText exerciseMinute;
    String amPMValue;
    private DBHelper DB;

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

        String currentYear = Integer.toString(yearInt);
        String currentMonth;
        String currentDay;
        String currentHour;
        String currentMinute;

        if (monthInt<10)
        {
            currentMonth = "0"+Integer.toString(monthInt);
        }
        else
        {
            currentMonth = Integer.toString(monthInt);
        }

        if (dayInt<10)
        {
            currentDay = "0"+Integer.toString(dayInt);
        }
        else
        {
            currentDay = Integer.toString(dayInt);
        }



        if (hourInt<10)
        {
            currentHour = "0"+Integer.toString(hourInt);
        }
        else
        {
            currentHour = Integer.toString(hourInt);
        }



        if (minuteInt<10)
        {
            currentMinute = "0"+Integer.toString(minuteInt);
        }
        else
        {
            currentMinute = Integer.toString(minuteInt);
        }

        DB = new DBHelper(this);
        setContentView(R.layout.activity_add_exercise);
        Spinner spinner = (Spinner) findViewById(R.id.amPMSelector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.am_pm_spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Button submitExerciseButton = (Button) findViewById(R.id.submitexerciseButton);

        exerciseDay = (EditText)findViewById(R.id.exerciseDay);
        exerciseDay.setText(currentDay);
        exerciseMonth = (EditText)findViewById(R.id.exerciseMonth);
        exerciseMonth.setText(currentMonth);
        exerciseYear = (EditText)findViewById(R.id.exerciseYear);
        exerciseYear.setText(currentYear);
        exerciseHour = (EditText)findViewById(R.id.exerciseHour);
        exerciseHour.setText(currentHour);
        exerciseMinute = (EditText)findViewById(R.id.exerciseMinute);
        exerciseMinute.setText(currentMinute);
        submitExerciseButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        exerciseReading = (EditText)findViewById(R.id.exerciseReading);
                                                        exerciseDay = (EditText)findViewById(R.id.exerciseDay);
                                                        exerciseMonth = (EditText)findViewById(R.id.exerciseMonth);
                                                        exerciseYear = (EditText)findViewById(R.id.exerciseYear);
                                                        exerciseHour = (EditText)findViewById(R.id.exerciseHour);
                                                        exerciseMinute = (EditText)findViewById(R.id.exerciseMinute);
                                                        Spinner spinner = (Spinner) findViewById(R.id.amPMSelector);
                                                        String value = exerciseReading.getText().toString();
                                                        String day = exerciseDay.getText().toString();
                                                        String month = exerciseMonth.getText().toString();
                                                        String year = exerciseYear.getText().toString();
                                                        String hour = exerciseHour.getText().toString();
                                                        String minute = exerciseMinute.getText().toString();
                                                        String amPMValue = spinner.getSelectedItem().toString();
                                                        Boolean inputValid = inputValidationExercise(day, month, year, hour, minute, value);
                                                        if (inputValid ==true)
                                                        {
                                                            DB.insertEntry("exerciseTable",value,day,month,year,hour,minute,amPMValue);
                                                            Log.i(tag, "exercise Entry Loaded into Database");
                                                            Intent startNewActivity = new Intent(getBaseContext(), MainActivity.class);
                                                            startActivityForResult(startNewActivity,10);
                                                        }
                                                        else
                                                        {
                                                            Log.i(tag, "Invalid input in add exercise activity");
                                                            AlertDialog.Builder customDialog = new AlertDialog.Builder(AddExerciseActivity.this);
                                                            LayoutInflater inflater = AddExerciseActivity.this.getLayoutInflater();
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
        if (month.length() !=2 || day.length() !=2 || minute.length() !=2 || year.length() !=4)
        {
            Log.i(tag, "Invalid formatting of day/month/year in update exercise activity");
            valid = false;
        }
        return valid;
    }
}