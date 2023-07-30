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

public class UpdateMedicationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String tag = "UpdateMedicationActivity";
    EditText medicationReading;
    EditText medicationDay;
    EditText medicationMonth;
    EditText medicationYear;
    EditText medicationHour;
    EditText medicationMinute;
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

        setContentView(R.layout.activity_update_medication);
        Spinner spinner = (Spinner) findViewById(R.id.amPMSelector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.am_pm_spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        int spinnerPosition = adapter.getPosition(amPMValue);
        spinner.setSelection(spinnerPosition);

        medicationDay = (EditText)findViewById(R.id.medicationDay);
        medicationMonth = (EditText)findViewById(R.id.medicationMonth);
        medicationYear = (EditText)findViewById(R.id.medicationYear);
        medicationReading = (EditText)findViewById(R.id.medicationReading);
        medicationHour = (EditText)findViewById(R.id.medicationHour);
        medicationMinute = (EditText)findViewById(R.id.medicationMinute);
        medicationReading.setText(entryValue);
        medicationDay.setText(entryDay);
        medicationMonth.setText(entryMonth);
        medicationYear.setText(entryYear);
        medicationHour.setText(entryHour);
        medicationMinute.setText(entryMinute);

        Button updateMedicationButton = (Button) findViewById(R.id.updatemedicationButton);
        updateMedicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(tag, "Updating Entry ID: "+entryID);
                String value = medicationReading.getText().toString();
                String day = medicationDay.getText().toString();
                String month = medicationMonth.getText().toString();
                String year = medicationYear.getText().toString();
                String hour = medicationHour.getText().toString();
                String minute = medicationMinute.getText().toString();
                String amPMValue = spinner.getSelectedItem().toString();
                Boolean valid = inputValidationMedication(day, month, year, hour, minute, value);

                Log.i(tag, "Day: "+day+" month: "+month+" year: "+year);
                DB.updateEntry("medicationTable", entryID, value, day, month, year, hour, minute, amPMValue);
                Log.i(tag, "Entry ID: "+entryID+" is updated");
                Intent startNewActivity = new Intent(getBaseContext(), MainActivity.class);
                startActivityForResult(startNewActivity,10);

            }
        });

        Button deleteMedicationButton = (Button) findViewById(R.id.deletemedicationButton);
        deleteMedicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(tag, "Deleting Entry ID: "+entryID);
                DB.deleteEntry("medicationTable", entryID);
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

    public boolean inputValidationMedication(String day, String month, String year, String hour, String minute, String value)
    {
        Boolean valid = true;
        DecimalFormat df = new DecimalFormat();

        if (Integer.parseInt(day)>31 ||Integer.parseInt(day)<1)
        {
            Log.i(tag, "Invalid day in update medication activity");
            valid=false;
        }
        if (Integer.parseInt(month)>12 ||Integer.parseInt(month)<1)
        {
            Log.i(tag, "Invalid month in update medication activity");
            valid = false;
        }
        if (Integer.parseInt(hour)>12 ||Integer.parseInt(hour)<0)
        {
            Log.i(tag, "Invalid hour in update medication activity");
            valid = false;
        }
        if (Integer.parseInt(minute)>59 ||Integer.parseInt(minute)<0)
        {
            Log.i(tag, "Invalid minute in update medication activity");
            valid = false;
        }
        if (month.length() !=2 || day.length() !=2 || hour.length() !=2|| minute.length() !=2 || year.length() !=4)
        {
            Log.i(tag, "Invalid formatting of day/month/year in update medication activity");
            valid = false;
        }
        return valid;
    }
}