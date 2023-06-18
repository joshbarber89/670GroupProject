package com.example.a670groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        DBHelper DB = new DBHelper(this);

        EditText emailAddressEditText = (EditText) findViewById(R.id.registerActivityEmailAddress);
        EditText passwordEditText = (EditText) findViewById(R.id.registerActivityPassword);
        EditText rePasswordEditText = (EditText) findViewById(R.id.registerActivityRePassword);

        Button register = (Button) findViewById(R.id.registerActivityButtonRegister);
        Button backToMain = (Button) findViewById(R.id.registerActivityButtonBack);

        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = emailAddressEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String rePassword = rePasswordEditText.getText().toString();

                Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
                Matcher emailMatcher = emailPattern.matcher(emailAddress);
                boolean validEmail = emailMatcher.find();

                if (emailAddress.equals("") || password.equals("") || rePassword.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Required Fields!", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(rePassword)) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                } else if (!validEmail) {
                    Toast.makeText(RegisterActivity.this, "Invalid Email!", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 5){
                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 Characters long!", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkIfExists = DB.checkIfUserExists(emailAddress);
                    if (!checkIfExists.equals(false)) {
                        Toast.makeText(RegisterActivity.this, "This user already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        Boolean insert = DB.insertData(emailAddress, password);
                        if (insert) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("Response", "User Registered!");
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}