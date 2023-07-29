package com.example.a670groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class StatsPageActivity extends AppCompatActivity {

    GraphView graphView;

    EditText dateText;
    EditText monthText;
    EditText yearText;

    ProgressBar progressBar;

    private DBHelper DB;
    ArrayList<String[]> entryArray = new ArrayList<String[]>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_page);



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

        Log.d("Stats", "Current date is "+currentDay+"-"+currentMonth+"-"+currentYear);


        dateText = (EditText)findViewById(R.id.editTextDate);
        dateText.setText(currentDay);
        monthText = (EditText)findViewById(R.id.editTextMonth);
        monthText.setText(currentMonth);
        yearText = (EditText)findViewById(R.id.editTextYear);
        yearText.setText(currentYear);
        progressBar = (ProgressBar) findViewById(R.id.statsProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        Button refreshButton = (Button) findViewById(R.id.statsRefreshData);
        DB = new DBHelper(this);


        refreshButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  dateText = (EditText)findViewById(R.id.editTextDate);
                  monthText = (EditText)findViewById(R.id.editTextMonth);
                  yearText = (EditText)findViewById(R.id.editTextYear);
                  String day = dateText.getText().toString();
                  String month = monthText.getText().toString();
                  String year = yearText.getText().toString();

                  Log.d("stats", "Getting blood sugar records for date "+day+"-"+month+"-"+year);
                  entryArray = DB.getEntries("bloodSugarTable", day, month, year);
                  Log.d("stats", "Entries Received from Database, displaying now");

                  StatsQuery stats = new StatsQuery();
                  stats.execute(entryArray);

              }
          }
        );
    }

    private class StatsQuery extends AsyncTask<ArrayList<String[]>, Void, ArrayList<String[]>> {

        @Override
        protected ArrayList<String[]> doInBackground(ArrayList<String[]>... arrayLists) {
            progressBar.setProgress(25);
            graphView = findViewById(R.id.idGraphView);
            ArrayList arrayList = arrayLists[0];
            DataPoint[] dp = new DataPoint[arrayList.size()];
            progressBar.setProgress(50);
            for (int i =0; i <arrayList.size(); i++)
            {
                String[] entryString = (String[]) arrayList.get(i);
                String entryValue = entryString[1];
                String hour = entryString[2];
                String minute = entryString[3];
                String amPm = entryString[4];
                try {
                    float entryFloatVal = Float.valueOf(entryValue);
                    float twentyFourHour;
                    if (amPm.equals("AM"))
                    {
                        if(Integer.parseInt(hour)==12)
                        {
                            twentyFourHour = 0+(Float.valueOf(minute)/60);
                        }
                        else {
                            twentyFourHour = Float.valueOf(hour)+(Float.valueOf(minute)/60);
                        }
                    }
                    else {
                        if(Integer.parseInt(hour)==12)
                        {
                            twentyFourHour = Float.valueOf(hour)+(Float.valueOf(minute)/60);
                        }
                        else {
                            twentyFourHour = Float.valueOf(hour)+12+(Float.valueOf(minute)/60);
                        }
                    }
                    dp[i] = new DataPoint(twentyFourHour, entryFloatVal);
                } catch (Exception e)
                {
                    Log.i("stats", "Invalid formatting of value - not shown in graph");
                }
            }
            progressBar.setProgress(75);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
            graphView.setTitle(getString(R.string.bloodSugarChartTitle));
            graphView.getGridLabelRenderer().setVerticalAxisTitleTextSize(18);
            graphView.getGridLabelRenderer().setVerticalAxisTitle(getString(R.string.bloodSugarYAxisTitle));
            graphView.getGridLabelRenderer().setHorizontalAxisTitleTextSize(18);
            graphView.getGridLabelRenderer().setHorizontalAxisTitle(getString(R.string.bloodSugarXAxisTitle));
            graphView.setTitleColor(R.color.black);
            graphView.setTitleTextSize(18);
            graphView.addSeries(series);
            progressBar.setProgress(100);
            progressBar.setVisibility(View.INVISIBLE);


            return null;
        }
    }
}