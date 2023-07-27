package com.example.a670groupproject;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.a670groupproject.databinding.ActivityMainBinding;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    String selected;

    EditText dateText;
    EditText monthText;
    EditText yearText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        int yearInt = cal.get(Calendar.YEAR);
        int monthInt = cal.get(Calendar.MONTH);
        int dayInt = cal.get(Calendar.DAY_OF_MONTH);

        String currentYear = Integer.toString(yearInt);
        String currentMonth;
        String currentDay;

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

        Log.d("main", "Current date is "+currentDay+"-"+currentMonth+"-"+currentYear);



        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Button foodButton = (Button) findViewById(R.id.foodButton);
        Button medicationButton = (Button) findViewById(R.id.medicationButton);
        Button exerciseButton = (Button) findViewById(R.id.exerciseButton);
        Button bloodSugarButton = (Button) findViewById(R.id.bloodSugarButton);
        Button insulinButton = (Button) findViewById(R.id.insulinButton);
        Button addEntry = (Button) findViewById(R.id.addEntryButton);


        dateText = (EditText)findViewById(R.id.editTextDate);
        dateText.setText(currentDay);
        monthText = (EditText)findViewById(R.id.editTextMonth);
        monthText.setText(currentMonth);
        yearText = (EditText)findViewById(R.id.editTextYear);
        yearText.setText(currentYear);

        selected = "Food";

        foodButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   selected = "Food";
                   Toast.makeText(MainActivity.this, "Showing values for "+selected, Toast.LENGTH_SHORT).show();
                   dateText = (EditText)findViewById(R.id.editTextDate);
                   monthText = (EditText)findViewById(R.id.editTextMonth);
                   yearText = (EditText)findViewById(R.id.editTextYear);
                   String day = dateText.getText().toString();
                   String month = monthText.getText().toString();
                   String year = yearText.getText().toString();
                   FoodFragment foodFragment = FoodFragment.newInstance(day, month,year);
                   FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                   ft.replace(R.id.flContainer,foodFragment);
                   ft.commit();
               }
           }
        );

        medicationButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  selected = "Medication";
                  Toast.makeText(MainActivity.this, "Showing values for "+selected, Toast.LENGTH_SHORT).show();
                  dateText = (EditText)findViewById(R.id.editTextDate);
                  monthText = (EditText)findViewById(R.id.editTextMonth);
                  yearText = (EditText)findViewById(R.id.editTextYear);
                  String day = dateText.getText().toString();
                  String month = monthText.getText().toString();
                  String year = yearText.getText().toString();
                  MedicationFragment medicationFragment = MedicationFragment.newInstance(day, month,year);
                  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                  ft.replace(R.id.flContainer,medicationFragment);
                  ft.commit();
              }
          }
        );

        exerciseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected = "Exercise";
                    Toast.makeText(MainActivity.this, "Showing values for "+selected, Toast.LENGTH_SHORT).show();
                    dateText = (EditText)findViewById(R.id.editTextDate);
                    monthText = (EditText)findViewById(R.id.editTextMonth);
                    yearText = (EditText)findViewById(R.id.editTextYear);
                    String day = dateText.getText().toString();
                    String month = monthText.getText().toString();
                    String year = yearText.getText().toString();
                    ExerciseFragment exerciseFragment = ExerciseFragment.newInstance(day, month,year);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flContainer,exerciseFragment);
                    ft.commit();
                }
            }
        );

        bloodSugarButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      selected = "BloodSugar";
                      Toast.makeText(MainActivity.this, "Showing values for "+selected, Toast.LENGTH_SHORT).show();
                      dateText = (EditText)findViewById(R.id.editTextDate);
                      monthText = (EditText)findViewById(R.id.editTextMonth);
                      yearText = (EditText)findViewById(R.id.editTextYear);
                      String day = dateText.getText().toString();
                      String month = monthText.getText().toString();
                      String year = yearText.getText().toString();
                      BloodSugarFragment bloodSugarFragment = BloodSugarFragment.newInstance(day, month,year);
                      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                      ft.replace(R.id.flContainer,bloodSugarFragment);
                      ft.commit();
                  }
              }
        );

        insulinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected = "Insulin";
                    Toast.makeText(MainActivity.this, "Showing values for "+selected, Toast.LENGTH_SHORT).show();
                    dateText = (EditText)findViewById(R.id.editTextDate);
                    monthText = (EditText)findViewById(R.id.editTextMonth);
                    yearText = (EditText)findViewById(R.id.editTextYear);
                    String day = dateText.getText().toString();
                    String month = monthText.getText().toString();
                    String year = yearText.getText().toString();
                    InsulinFragment insulinFragment = InsulinFragment.newInstance(day, month,year);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flContainer,insulinFragment);
                    ft.commit();
                }
            }
        );

        addEntry.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                    Intent startNewActivity;
                    dateText = (EditText)findViewById(R.id.editTextDate);
                    monthText = (EditText)findViewById(R.id.editTextMonth);
                    yearText = (EditText)findViewById(R.id.editTextYear);
                    String day = dateText.getText().toString();
                    String month = monthText.getText().toString();
                    String year = yearText.getText().toString();
                    Boolean inputValidation = inputValidationMain(day, month, year);
                    if (inputValidation == true)
                    {
                        switch (selected) {
                            case "Food":
                                Log.d("main", "starting add food");
                                //start 'add food' activity
                                break;
                            case "Medication":
                                Log.d("main", "starting add medication");
                                //start 'add medication' activity
                                break;
                            case "Exercise":
                                Log.d("main", "starting add exercise");
                                //start 'add exercise' activity
                                break;
                            case "BloodSugar":
                                Log.d("main", "starting add blood sugar");
                                startNewActivity = new Intent(getBaseContext(), AddBloodSugarActivity.class);
                                startActivityForResult(startNewActivity, 10);
                                break;
                            case "Insulin":
                                Log.d("main", "starting add insulin");
                                //start 'add insulin' activity
                                break;
                        }
                    }
                    else
                    {
                        Snackbar.make(v, "Please input a valid date", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                 }
             }
        );

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.toolbar);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public boolean onCreateOptionsMenu (Menu m)
    {
        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi)
    {
        int id = mi.getItemId();
        Intent startNewActivity;
        switch (id) {
            case R.id.settingsMenuButton:
                Log.d("Toolbar", "Settings selected");
                startNewActivity = new Intent(getBaseContext(), SettingsActivity.class);
                startActivityForResult(startNewActivity,10);
                break;
            case R.id.statsMenuButton:
                Log.d("Toolbar", "Stats selected");
                startNewActivity = new Intent(getBaseContext(), StatsPageActivity.class);
                startActivityForResult(startNewActivity,10);
                break;
            case R.id.aboutMenuButton:
                Log.d("Toolbar", "About selected");
                startNewActivity = new Intent(getBaseContext(), AboutActivity.class);
                startActivityForResult(startNewActivity,10);
                break;
        }
        return true;
    }
    public boolean inputValidationMain(String day, String month, String year)
    {
        Boolean valid = true;

        if (Integer.parseInt(day)>31 ||Integer.parseInt(day)<1)
        {
            valid=false;
        }
        if (Integer.parseInt(month)>12 ||Integer.parseInt(month)<1)
        {
            valid = false;
        }
        if (month.length() !=2 || day.length() !=2 || year.length() !=4)
        {
            valid = false;
        }
        return valid;
    }

}