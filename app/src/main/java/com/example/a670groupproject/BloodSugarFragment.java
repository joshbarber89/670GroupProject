package com.example.a670groupproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BloodSugarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BloodSugarFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String mParam3;

    ListView bloodSugarList;

    private DBHelper DB;
    ArrayList<String[]> entryArray = new ArrayList<String[]>();

    View view;

    public BloodSugarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BloodSugarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BloodSugarFragment newInstance(String param1, String param2, String param3) {
        BloodSugarFragment fragment = new BloodSugarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
        //todo: populate the list in the fragment with entries from the database



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blood_sugar, container, false);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
        DB = new DBHelper(getActivity());
        Log.d("Blood Sugar Fragment", "Getting blood sugar records for date "+mParam1+"-"+mParam2+"-"+mParam3);
        entryArray = DB.getEntries("bloodSugarTable", mParam1, mParam2, mParam3);
        Log.d("Blood Sugar Fragment", "Entries Received from Database, displaying now");
        EntryAdapter entryAdapter = new EntryAdapter(getActivity());
        bloodSugarList = (ListView) view.findViewById(R.id.bloodSugarList);
        bloodSugarList.setAdapter(entryAdapter);

        bloodSugarList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("Blood Sugar Fragment", "item clicked at position: "+i);
                String[] entryString = entryArray.get(i);
                String entryID = entryString[0];
                String entryValue = entryString[1];
                String entryHour = entryString[2];
                String entryMinute = entryString[3];
                String amPM = entryString[4];
                Intent startNewActivity = new Intent(getActivity().getBaseContext(), UpdateBloodSugarActivity.class);
                startNewActivity.putExtra("entryID", entryID);
                startNewActivity.putExtra("entryValue", entryValue);
                startNewActivity.putExtra("entryHour", entryHour);
                startNewActivity.putExtra("entryMinute", entryMinute);
                startNewActivity.putExtra("amPM", amPM);
                startNewActivity.putExtra("entryDay", mParam1);
                startNewActivity.putExtra("entryMonth", mParam2);
                startNewActivity.putExtra("entryYear", mParam3);
                startActivityForResult(startNewActivity,10);
            }
        });


        return view;
    }

    @Override
    public void onClick(View view) {
        //when someone clicks on the list, show details
    }

    private class EntryAdapter extends ArrayAdapter<String[]>
    {
        public EntryAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount()
        {
            int listSize = entryArray.size();
            return listSize;
        }
        public String[] getItem(int position)
        {
            String[] entry = entryArray.get(position);
            return entry;
        }
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = BloodSugarFragment.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.entry_row, null);
            TextView entryValue = (TextView)result.findViewById(R.id.entryValue);
            TextView entryHour = (TextView)result.findViewById(R.id.entryHour);
            TextView entryMinute = (TextView)result.findViewById(R.id.entryMinute);
            TextView entryAMPM = (TextView)result.findViewById(R.id.entryAMPM);
            entryValue.setText("Blood Sugar:"+getItem(position)[1]);
            entryHour.setText(getItem(position)[2]);
            entryMinute.setText(getItem(position)[3]);
            entryAMPM.setText(getItem(position)[4]);
            return result;
        }
    }

}