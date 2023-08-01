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
class AddBloodSugarHelper {

    private static final String tag = "AddBloodSugarActivity";
    private static final String TABLE1 = "bloodSugarTable0";
    private static final String TABLE2 = "bloodSugarTable1";
    protected DBHelper DB;

    public boolean insert(String day, String month, String year, String hour, String minute, String value, String amPMValue) {
        if (this.inputValidation(day, month, year, hour, minute, value)) {

            if (MainActivity.selectedBloodSugarUnit == SettingsActivity.BloodSugarUnit_mmolPerLitre) {
                DB.insertEntry(TABLE1, value, day, month, year, hour, minute, amPMValue);
                DB.insertEntry(TABLE2, (Float.parseFloat(value) * 18.018) + "", day, month, year, hour, minute, amPMValue);
            } else {
                DB.insertEntry(TABLE2, value, day, month, year, hour, minute, amPMValue);
                DB.insertEntry(TABLE1, (Float.parseFloat(value) * 0.0555) + "", day, month, year, hour, minute, amPMValue);
            }
            Log.i(tag, "Blood Sugar Entry Loaded into Database");
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

    public String datetimeModification(int value) {
        if (value<10) {
             return "0"+value;
        }
        return String.valueOf(value);
    }
}
public class AddBloodSugarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String tag = "AddBloodSugarActivity";
    EditText bloodSugarReading;
    EditText bloodSugarDay;
    EditText bloodSugarMonth;
    EditText bloodSugarYear;
    EditText bloodSugarHour;
    EditText bloodSugarMinute;
    String amPMValue;
    AddBloodSugarHelper absh = new AddBloodSugarHelper();

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

        absh.DB = new DBHelper(this);
        setContentView(R.layout.activity_add_blood_sugar);
        Spinner spinner = (Spinner) findViewById(R.id.amPMSelector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.am_pm_spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Button submitBloodSugarButton = (Button) findViewById(R.id.submitBloodSugarButton);

        bloodSugarDay = (EditText)findViewById(R.id.bloodSugarDay);
        bloodSugarDay.setText(absh.datetimeModification(dayInt));
        bloodSugarMonth = (EditText)findViewById(R.id.bloodSugarMonth);
        bloodSugarMonth.setText(absh.datetimeModification(monthInt));
        bloodSugarYear = (EditText)findViewById(R.id.bloodSugarYear);
        bloodSugarYear.setText(String.valueOf(yearInt));
        bloodSugarHour = (EditText)findViewById(R.id.bloodSugarHour);
        bloodSugarHour.setText(absh.datetimeModification(hourInt));
        bloodSugarMinute = (EditText)findViewById(R.id.bloodSugarMinute);
        bloodSugarMinute.setText(absh.datetimeModification(minuteInt));
        submitBloodSugarButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  bloodSugarReading = (EditText)findViewById(R.id.bloodSugarReading);
                  bloodSugarDay = (EditText)findViewById(R.id.bloodSugarDay);
                  bloodSugarMonth = (EditText)findViewById(R.id.bloodSugarMonth);
                  bloodSugarYear = (EditText)findViewById(R.id.bloodSugarYear);
                  bloodSugarHour = (EditText)findViewById(R.id.bloodSugarHour);
                  bloodSugarMinute = (EditText)findViewById(R.id.bloodSugarMinute);
                  Spinner spinner = (Spinner) findViewById(R.id.amPMSelector);
                  String value = bloodSugarReading.getText().toString();
                  String day = bloodSugarDay.getText().toString();
                  String month = bloodSugarMonth.getText().toString();
                  String year = bloodSugarYear.getText().toString();
                  String hour = bloodSugarHour.getText().toString();
                  String minute = bloodSugarMinute.getText().toString();
                  String amPMValue = spinner.getSelectedItem().toString();
                  boolean inputValid = absh.insert(day, month, year, hour, minute, value, amPMValue);
                  if (inputValid) {
                      Intent startNewActivity = new Intent(getBaseContext(), MainActivity.class);
                      startActivityForResult(startNewActivity, 10);
                  }
                  else
                  {
                      Log.i(tag, "Invalid input in add blood sugar activity");
                      AlertDialog.Builder customDialog = new AlertDialog.Builder(AddBloodSugarActivity.this);
                      LayoutInflater inflater = AddBloodSugarActivity.this.getLayoutInflater();
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