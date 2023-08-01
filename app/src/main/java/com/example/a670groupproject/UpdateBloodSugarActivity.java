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

class UpdateBloodSugarHelper {

    private static final String tag = "UpdateBloodSugarActivity";
    private static final String TABLE1 = "bloodSugarTable0";
    private static final String TABLE2 = "bloodSugarTable1";
    protected DBHelper DB;
    public boolean update(String entryID, String day, String month, String year, String hour, String minute, String value, String amPMValue) {
        if (this.inputValidation(day, month, year, hour, minute, value)) {

            if (MainActivity.selectedBloodSugarUnit == SettingsActivity.BloodSugarUnit_mmolPerLitre) {
                DB.updateEntry(TABLE1, entryID, value, day, month, year, hour, minute, amPMValue);
                DB.updateEntry(TABLE2, entryID, (Float.parseFloat(value) * 18.018) + "", day, month, year, hour, minute, amPMValue);
            } else {
                DB.updateEntry(TABLE2, entryID, value, day, month, year, hour, minute, amPMValue);
                DB.updateEntry(TABLE1, entryID, (Float.parseFloat(value) * 0.0555) + "", day, month, year, hour, minute, amPMValue);
            }

            return true;
        }
        return false;
    }
    public boolean delete(String entryID) {
        Log.i(tag, "Deleting Entry ID: " + entryID);
        DB.deleteEntry(TABLE1, entryID);
        DB.deleteEntry(TABLE2, entryID);
        Log.i(tag, "Entry ID: " + entryID + " is deleted");
        return true;
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
        if (month.length() !=2 || day.length() !=2 || minute.length() !=2 || year.length() !=4)
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

    Spinner spinner;
    protected UpdateBloodSugarHelper ubsh = new UpdateBloodSugarHelper();

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
        ubsh.DB = new DBHelper(this);

        setContentView(R.layout.activity_update_blood_sugar);
        spinner = (Spinner) findViewById(R.id.amPMSelector);
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
                updateBloodSugarButtonClicked(v);
            }
        });

        Button deleteBloodSugarButton = (Button) findViewById(R.id.deleteBloodSugarButton);
        deleteBloodSugarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBloodSugarButtonClicked(v);
            }
        });

    }

    public void deleteBloodSugarButtonClicked(View v) {
        ubsh.delete(entryID);
        Intent startNewActivity = new Intent(getBaseContext(), MainActivity.class);
        startActivityForResult(startNewActivity, 10);
    }

    public void updateBloodSugarButtonClicked(View v) {
        Log.i(tag, "Updating Entry ID: "+entryID);
        String value = bloodSugarReading.getText().toString();
        String day = bloodSugarDay.getText().toString();
        String month = bloodSugarMonth.getText().toString();
        String year = bloodSugarYear.getText().toString();
        String hour = bloodSugarHour.getText().toString();
        String minute = bloodSugarMinute.getText().toString();
        String amPMValue = spinner.getSelectedItem().toString();

        Log.i(tag, "Day: "+day+" month: "+month+" year: "+year);

        boolean updated = ubsh.update(entryID, day, month, year, hour, minute, value, amPMValue);
        if (updated) {
            Log.i(tag, "Entry ID: " + entryID + " is updated");
            Intent startNewActivity = new Intent(getBaseContext(), MainActivity.class);
            startActivityForResult(startNewActivity, 10);
        }
        else
        {
            Log.i(tag, "Invalid input in update blood sugar activity");
            AlertDialog.Builder customDialog = new AlertDialog.Builder(UpdateBloodSugarActivity.this);
            LayoutInflater inflater = UpdateBloodSugarActivity.this.getLayoutInflater();
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        amPMValue = (String) adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        amPMValue = "AM";
    }
}