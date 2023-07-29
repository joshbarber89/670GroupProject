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

public class AddFoodActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String tag = "AddFoodActivity";
    EditText foodReading;
    EditText foodDay;
    EditText foodMonth;
    EditText foodYear;
    EditText foodHour;
    EditText foodMinute;
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
        setContentView(R.layout.activity_add_food);
        Spinner spinner = (Spinner) findViewById(R.id.amPMSelector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.am_pm_spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Button submitFoodButton = (Button) findViewById(R.id.submitfoodButton);

        foodDay = (EditText)findViewById(R.id.foodDay);
        foodDay.setText(currentDay);
        foodMonth = (EditText)findViewById(R.id.foodMonth);
        foodMonth.setText(currentMonth);
        foodYear = (EditText)findViewById(R.id.foodYear);
        foodYear.setText(currentYear);
        foodHour = (EditText)findViewById(R.id.foodHour);
        foodHour.setText(currentHour);
        foodMinute = (EditText)findViewById(R.id.foodMinute);
        foodMinute.setText(currentMinute);
        submitFoodButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    foodReading = (EditText)findViewById(R.id.foodReading);
                    foodDay = (EditText)findViewById(R.id.foodDay);
                    foodMonth = (EditText)findViewById(R.id.foodMonth);
                    foodYear = (EditText)findViewById(R.id.foodYear);
                    foodHour = (EditText)findViewById(R.id.foodHour);
                    foodMinute = (EditText)findViewById(R.id.foodMinute);
                    Spinner spinner = (Spinner) findViewById(R.id.amPMSelector);
                    String value = foodReading.getText().toString();
                    String day = foodDay.getText().toString();
                    String month = foodMonth.getText().toString();
                    String year = foodYear.getText().toString();
                    String hour = foodHour.getText().toString();
                    String minute = foodMinute.getText().toString();
                    String amPMValue = spinner.getSelectedItem().toString();
                    Boolean inputValid = inputValidationFood(day, month, year, hour, minute, value);
                    if (inputValid ==true)
                    {
                        DB.insertEntry("foodTable",value,day,month,year,hour,minute,amPMValue);
                        Log.i(tag, "Food Entry Loaded into Database");
                        Intent startNewActivity = new Intent(getBaseContext(), MainActivity.class);
                        startActivityForResult(startNewActivity,10);
                    }
                    else
                    {
                        Log.i(tag, "Invalid input in add blood sugar activity");
                        AlertDialog.Builder customDialog = new AlertDialog.Builder(AddFoodActivity.this);
                        LayoutInflater inflater = AddFoodActivity.this.getLayoutInflater();
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

    public boolean inputValidationFood(String day, String month, String year, String hour, String minute, String value)
    {
        Boolean valid = true;
        DecimalFormat df = new DecimalFormat();

        if (Integer.parseInt(day)>31 ||Integer.parseInt(day)<1)
        {
            Log.i(tag, "Invalid day in update food activity");
            valid=false;
        }
        if (Integer.parseInt(month)>12 ||Integer.parseInt(month)<1)
        {
            Log.i(tag, "Invalid month in update food activity");
            valid = false;
        }
        if (Integer.parseInt(hour)>12 ||Integer.parseInt(hour)<0)
        {
            Log.i(tag, "Invalid hour in update food activity");
            valid = false;
        }
        if (Integer.parseInt(minute)>59 ||Integer.parseInt(minute)<0)
        {
            Log.i(tag, "Invalid minute in update food activity");
            valid = false;
        }
        if (month.length() !=2 || day.length() !=2 || hour.length() !=2|| minute.length() !=2 || year.length() !=4)
        {
            Log.i(tag, "Invalid formatting of day/month/year in update food activity");
            valid = false;
        }
        return valid;
    }
}