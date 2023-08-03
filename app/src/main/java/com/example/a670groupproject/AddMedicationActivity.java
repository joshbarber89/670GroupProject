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

class AddMedicationHelper {

    private static final String tag = "AddMedicationActivity";
    private static final String TABLE = "medicationTable";

    protected DBHelper DB;

    public boolean insert(String day, String month, String year, String hour, String minute, String value, String amPMValue) {
        if (this.inputValidation(day, month, year, hour, minute, value)) {

            DB.insertEntry(TABLE, value, day, month, year, hour, minute, amPMValue);

            Log.i(tag, TABLE + " Entry Loaded into Database");
            return true;
        }
        return false;
    }
    private boolean inputValidation(String day, String month, String year, String hour, String minute, String value)
    {
        boolean valid = true;

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

        return valid;
    }

    public String datetimeModification(int value) {
        if (value<10) {
            return "0"+value;
        }
        return String.valueOf(value);
    }
}
public class AddMedicationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String tag = "AddMedicationActivity";
    EditText medicationReading;
    EditText medicationDay;
    EditText medicationMonth;
    EditText medicationYear;
    EditText medicationHour;
    EditText medicationMinute;
    String amPMValue;

    AddMedicationHelper amh = new AddMedicationHelper();
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

        amh.DB = new DBHelper(this);
        setContentView(R.layout.activity_add_medication);
        Spinner spinner = (Spinner) findViewById(R.id.amPMSelector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.am_pm_spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Button submitMedicationButton = (Button) findViewById(R.id.submitmedicationButton);

        medicationDay = (EditText)findViewById(R.id.medicationDay);
        medicationDay.setText(amh.datetimeModification(dayInt));
        medicationMonth = (EditText)findViewById(R.id.medicationMonth);
        medicationMonth.setText(amh.datetimeModification(monthInt));
        medicationYear = (EditText)findViewById(R.id.medicationYear);
        medicationYear.setText(String.valueOf(yearInt));
        medicationHour = (EditText)findViewById(R.id.medicationHour);
        medicationHour.setText(amh.datetimeModification(hourInt));
        medicationMinute = (EditText)findViewById(R.id.medicationMinute);
        medicationMinute.setText(amh.datetimeModification(minuteInt));
        submitMedicationButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              medicationReading = (EditText)findViewById(R.id.medicationReading);
              medicationDay = (EditText)findViewById(R.id.medicationDay);
              medicationMonth = (EditText)findViewById(R.id.medicationMonth);
              medicationYear = (EditText)findViewById(R.id.medicationYear);
              medicationHour = (EditText)findViewById(R.id.medicationHour);
              medicationMinute = (EditText)findViewById(R.id.medicationMinute);
              Spinner spinner = (Spinner) findViewById(R.id.amPMSelector);
              String value = medicationReading.getText().toString();
              String day = medicationDay.getText().toString();
              String month = medicationMonth.getText().toString();
              String year = medicationYear.getText().toString();
              String hour = medicationHour.getText().toString();
              String minute = medicationMinute.getText().toString();
              String amPMValue = spinner.getSelectedItem().toString();
              boolean inputValid = amh.insert(day, month, year, hour, minute, value, amPMValue);
              if (inputValid) {
                  Intent startNewActivity = new Intent(getBaseContext(), MainActivity.class);
                  startActivityForResult(startNewActivity,10);
              }
              else
              {
                  Log.i(tag, "Invalid input in add medication activity");
                  AlertDialog.Builder customDialog = new AlertDialog.Builder(AddMedicationActivity.this);
                  LayoutInflater inflater = AddMedicationActivity.this.getLayoutInflater();
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