package com.example.a670groupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;

public class LoginActivity extends AppCompatActivity {

    public static final String tag = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(tag, "onCreate function has been called");

        //helper method
        loadUserData();
    }

    private void loadUserData() {
        String preference_file_name = getString(R.string.preference_name);
        SharedPreferences mPrefs = getSharedPreferences(preference_file_name, MODE_PRIVATE);
        String  email_key = getString(R.string.key_email);
        String new_email_value = mPrefs.getString(email_key, "email@domain.com");
        ((EditText) findViewById(R.id.login_name)).setText(new_email_value);
    }

    public void onLoginClicked(View v){
        String new_email_entered = ((EditText) findViewById(R.id.login_name)).getText().toString();
        String password_entered = ((EditText) findViewById(R.id.password)).getText().toString();

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(new_email_entered).matches()==true && password_entered.matches("")==false)
        {
            String file_name = getString(R.string.preference_name);
            SharedPreferences mPrefs = getSharedPreferences(file_name, MODE_PRIVATE);
            SharedPreferences.Editor myEditor = mPrefs.edit();
            myEditor.clear();
            String email_key  = getString(R.string.key_email);
            myEditor.putString(email_key, new_email_entered);
            myEditor.commit();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.loginError); // Text

            // Add the buttons
            builder.setPositiveButton(R.string.ok, new /*positive Button*/
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id){
                            // User clicked OK button
                        }
                    });
            AlertDialog dialog = builder.create();

            dialog.show();
        }


    }


}