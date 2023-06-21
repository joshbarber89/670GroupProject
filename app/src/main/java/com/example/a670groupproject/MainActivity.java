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
import android.widget.EditText;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.a670groupproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

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
}