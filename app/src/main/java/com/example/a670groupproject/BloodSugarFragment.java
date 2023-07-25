package com.example.a670groupproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    public static BloodSugarFragment newInstance(String param1, String param2) {
        BloodSugarFragment fragment = new BloodSugarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //todo: populate the list in the fragment with entries from the database

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blood_sugar, container, false);
        Button addBloodSugarButton = (Button) view.findViewById(R.id.addNewBloodSugarButton);
        addBloodSugarButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent startNewActivity = new Intent(getActivity(), AddBloodSugarActivity.class);
        startActivityForResult(startNewActivity,10);
    }
}