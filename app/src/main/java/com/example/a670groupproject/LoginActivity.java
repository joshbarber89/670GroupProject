package com.example.a670groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    public static final String tag = "LoginActivity";

    private static DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(tag, "onCreate function has been called");

        DB = new DBHelper(this);

        loadUserData();

        Button loginButton = (Button) findViewById(R.id.loginActivityButtonLogin);
        Button registerButton = (Button) findViewById(R.id.loginActivityButtonRegister);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = ((EditText) findViewById(R.id.loginActivityEditTextEmailAddress)).getText().toString();
                String password = ((EditText) findViewById(R.id.loginActivityEditTextPassword)).getText().toString();

                if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()
                                && !password.matches("")
                ) {
                    CheckBox rememberMeCheckBox = (CheckBox) findViewById(R.id.loginActivityCheckboxRememberMe);
                    SharedPreferences loginPrefs = getSharedPreferences("loginPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor myEditor = loginPrefs.edit();
                    myEditor.clear();

                    if (rememberMeCheckBox.isChecked()) {
                        myEditor.putString("rememberMe", "true");
                        myEditor.putString("email", emailAddress);
                        myEditor.putString("password", password);

                    } else {
                        myEditor.putString("rememberMe", "false");
                    }

                    myEditor.apply();

                    loginProcess(emailAddress, password);

                } else {
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), "Incorrect login", duration);
                    toast.show();
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(registerIntent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Log.i(tag, "Returned to MainActivity.onActivityResult");
            String messagePassed = data.getStringExtra("Response");
            if (messagePassed != null && !messagePassed.equals("")) {
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(this, messagePassed, duration);
                toast.show();
            }
        }
    }

    private void loadUserData() {
        CheckBox rememberMeCheckBox = (CheckBox) findViewById(R.id.loginActivityCheckboxRememberMe);
        SharedPreferences loginPrefs = getSharedPreferences("loginPreferences", MODE_PRIVATE);
        String rememberMeString = loginPrefs.getString("rememberMe", "false");
        String loggedOutString = loginPrefs.getString("loggedOut", "false");

        boolean rememberMe = rememberMeString.equals("true");
        boolean loggedOut = loggedOutString.equals("true");
        rememberMeCheckBox.setChecked(rememberMe);

        String emailAddress = loginPrefs.getString("email", "email@domain.com");
        String password = loginPrefs.getString("password", "");

        if (loggedOut) {
            ((EditText) findViewById(R.id.loginActivityEditTextEmailAddress)).setText(emailAddress);
            ((EditText) findViewById(R.id.loginActivityEditTextPassword)).setText(password);
        } else if (rememberMe) {
            loginProcess(emailAddress, password);
        } else {
            SharedPreferences.Editor myEditor = loginPrefs.edit();
            myEditor.clear();
            myEditor.putString("rememberMe", "false");
            myEditor.apply();
        }
    }

    private void loginProcess(String emailAddress, String password) {
        if (DB.checkUsernameAndPassword(emailAddress, password)) {

            SharedPreferences userPrefs = getSharedPreferences("userPreferences", MODE_PRIVATE);
            SharedPreferences.Editor myEditor = userPrefs.edit();
            myEditor.putString("emailAddress", emailAddress);
            myEditor.apply();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getApplicationContext(), "Incorrect login", duration);
            toast.show();
        }
    }
}