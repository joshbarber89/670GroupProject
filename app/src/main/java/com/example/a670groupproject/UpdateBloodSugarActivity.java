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

public class UpdateBloodSugarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String tag = "UpdateBloodSugarActivity";
    EditText bloodSugarReading;
    EditText bloodSugarDay;
    EditText bloodSugarMonth;
    EditText bloodSugarYear;
    EditText bloodSugarHour;
    EditText bloodSugarMinute;
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

        setContentView(R.layout.activity_update_blood_sugar);
        Spinner spinner = (Spinner) findViewById(R.id.amPMSelector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.am_pm_spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        int spinnerPosition = adapter.getPosition(amPMValue);
        spinner.setSelection(spinnerPosition);

        bloodSugarDay = (EditText)findViewById(R.id.bloodSugarDay);
        bloodSugarMonth = (EditText)findViewById(R.id.bloodSugarMonth);
        bloodSugarYear = (EditText)findViewById(R.id.bloodSugarYear);
        bloodSugarReading = (EditText)findViewById(R.id.bloodSugarReading);
        bloodSugarHour = (EditText)findViewById(R.id.bloodSugarHour);
        bloodSugarMinute = (EditText)findViewById(R.id.bloodSugarMinute);
        bloodSugarReading.setText(entryValue);
        bloodSugarDay.setText(entryDay);
        bloodSugarMonth.setText(entryMonth);
        bloodSugarYear.setText(entryYear);
        bloodSugarHour.setText(entryHour);
        bloodSugarMinute.setText(entryMinute);

        Button updateBloodSugarButton = (Button) findViewById(R.id.updateBloodSugarButton);
        updateBloodSugarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(tag, "Updating Entry ID: "+entryID);
                String value = bloodSugarReading.getText().toString();
                String day = bloodSugarDay.getText().toString();
                String month = bloodSugarMonth.getText().toString();
                String year = bloodSugarYear.getText().toString();
                String hour = bloodSugarHour.getText().toString();
                String minute = bloodSugarMinute.getText().toString();
                String amPMValue = spinner.getSelectedItem().toString();
                Boolean valid = inputValidationBloodSugar(day, month, year, hour, minute, value);

                Log.i(tag, "Day: "+day+" month: "+month+" year: "+year);

                if (MainActivity.selectedBloodSugarUnit == SettingsActivity.BloodSugarUnit_mmolPerLitre) {

                    DB.updateEntry("bloodSugarTable0", entryID, value, day, month, year, hour, minute, amPMValue);

                    DB.updateEntry("bloodSugarTable1", entryID,(Float.parseFloat(value) * 18.018) + "", day, month, year, hour, minute, amPMValue);

                    Log.i(tag, "Entry ID: " + entryID + " is updated");
                    Intent startNewActivity = new Intent(getBaseContext(), MainActivity.class);
                    startActivityForResult(startNewActivity, 10);
                }
                else{
                    DB.updateEntry("bloodSugarTable1", entryID, value, day, month, year, hour, minute, amPMValue);

                    DB.updateEntry("bloodSugarTable0", entryID,(Float.parseFloat(value) * 0.0555) + "", day, month, year, hour, minute, amPMValue);

                    Log.i(tag, "Entry ID: " + entryID + " is updated");
                    Intent startNewActivity = new Intent(getBaseContext(), MainActivity.class);
                    startActivityForResult(startNewActivity, 10);
                }

            }
        });

        Button deleteBloodSugarButton = (Button) findViewById(R.id.deleteBloodSugarButton);
        deleteBloodSugarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(tag, "Deleting Entry ID: "+entryID);
                DB.deleteEntry("bloodSugarTable0", entryID);
                DB.deleteEntry("bloodSugarTable1", entryID);
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

    public boolean inputValidationBloodSugar(String day, String month, String year, String hour, String minute, String value)
    {
        Boolean valid = true;
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
        if (month.length() !=2 || day.length() !=2 || hour.length() !=2|| minute.length() !=2 || year.length() !=4)
        {
            Log.i(tag, "Invalid formatting of day/month/year in update blood sugar activity");
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
}