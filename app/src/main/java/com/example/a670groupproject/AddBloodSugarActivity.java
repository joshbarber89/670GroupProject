package com.example.a670groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddBloodSugarActivity extends AppCompatActivity {

    EditText bloodSugarReading;
    EditText bloodSugarTime;
    CalendarView calendarView;
    String curDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blood_sugar);


        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        curDate = sdf.format(date.getTime());

        Button submitBloodSugarButton = (Button) findViewById(R.id.submitBloodSugarButton);
        submitBloodSugarButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  bloodSugarReading = (EditText)findViewById(R.id.bloodSugarReading);
                  bloodSugarTime = (EditText)findViewById(R.id.bloodSugarTime);
                    //todo: put blood sugar reading, time, and current date into a database
                  Intent startNewActivity = new Intent(getBaseContext(), MainActivity.class);
                  startActivityForResult(startNewActivity,10);
              }
          }
        );
        CalendarView calendarView = (CalendarView) findViewById(R.id.bloodSugarCalendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                curDate = String.valueOf(dayOfMonth);
            }
        });
    }
}