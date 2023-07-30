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

public class UpdateFoodActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String tag = "UpdateFoodActivity";
    EditText foodReading;
    EditText foodDay;
    EditText foodMonth;
    EditText foodYear;
    EditText foodHour;
    EditText foodMinute;
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

        setContentView(R.layout.activity_update_food);
        Spinner spinner = (Spinner) findViewById(R.id.amPMSelector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.am_pm_spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        int spinnerPosition = adapter.getPosition(amPMValue);
        spinner.setSelection(spinnerPosition);

        foodDay = (EditText)findViewById(R.id.foodDay);
        foodMonth = (EditText)findViewById(R.id.foodMonth);
        foodYear = (EditText)findViewById(R.id.foodYear);
        foodReading = (EditText)findViewById(R.id.foodReading);
        foodHour = (EditText)findViewById(R.id.foodHour);
        foodMinute = (EditText)findViewById(R.id.foodMinute);
        foodReading.setText(entryValue);
        foodDay.setText(entryDay);
        foodMonth.setText(entryMonth);
        foodYear.setText(entryYear);
        foodHour.setText(entryHour);
        foodMinute.setText(entryMinute);

        Button updateFoodButton = (Button) findViewById(R.id.updatefoodButton);
        updateFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(tag, "Updating Entry ID: "+entryID);
                String value = foodReading.getText().toString();
                String day = foodDay.getText().toString();
                String month = foodMonth.getText().toString();
                String year = foodYear.getText().toString();
                String hour = foodHour.getText().toString();
                String minute = foodMinute.getText().toString();
                String amPMValue = spinner.getSelectedItem().toString();
                Boolean valid = inputValidationFood(day, month, year, hour, minute, value);
                if (valid==true) {
                    Log.i(tag, "Day: " + day + " month: " + month + " year: " + year);
                    DB.updateEntry("foodTable", entryID, value, day, month, year, hour, minute, amPMValue);
                    Log.i(tag, "Entry ID: " + entryID + " is updated");
                    Intent startNewActivity = new Intent(getBaseContext(), MainActivity.class);
                    startActivityForResult(startNewActivity, 10);
                }
                else
                {
                    Log.i(tag, "Invalid input in food activity");
                    AlertDialog.Builder customDialog = new AlertDialog.Builder(UpdateFoodActivity.this);
                    LayoutInflater inflater = UpdateFoodActivity.this.getLayoutInflater();
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
        });

        Button deleteFoodButton = (Button) findViewById(R.id.deletefoodButton);
        deleteFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(tag, "Deleting Entry ID: "+entryID);
                DB.deleteEntry("foodTable", entryID);
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
        if (month.length() !=2 || day.length() !=2 || minute.length() !=2 || year.length() !=4)
        {
            Log.i(tag, "Invalid formatting of day/month/year in update food activity");
            valid = false;
        }
        return valid;
    }
}