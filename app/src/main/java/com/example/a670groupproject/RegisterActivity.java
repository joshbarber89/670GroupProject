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

class RegisterHelper {
    protected DBHelper DB;
    public int validateRegistration(String emailAddress, String password, String rePassword) {
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        Matcher emailMatcher = emailPattern.matcher(emailAddress);
        boolean validEmail = emailMatcher.find();

        if (emailAddress.equals("") || password.equals("") || rePassword.equals("")) {
            return 2;
        } else if (!password.equals(rePassword)) {
            return 3;
        } else if (!validEmail) {
            return 4;
        } else if (password.length() < 5){
            return 5;
        } else {
            Boolean checkIfExists = DB.checkIfUserExists(emailAddress);
            if (!checkIfExists.equals(false)) {
                return 6;
            } else {
                Boolean insert = DB.insertData(emailAddress, password);
                if (insert) {
                    return 1;
                } else {
                    return 7;
                }
            }
        }
    }
}
public class RegisterActivity extends AppCompatActivity {

    RegisterHelper rh = new RegisterHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rh.DB = new DBHelper(this);

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
                int results = rh.validateRegistration(emailAddress, password, rePassword);
                switch (results) {
                    case 1:
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("Response", "User Registered!");
                        setResult(RESULT_OK, resultIntent);
                        finish();
                        break;
                    case 2:
                        Toast.makeText(RegisterActivity.this, "Please Enter Required Fields!", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(RegisterActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(RegisterActivity.this, "Invalid Email!", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(RegisterActivity.this, "Password must be at least 6 Characters long!", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(RegisterActivity.this, "This user already exists", Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        Toast.makeText(RegisterActivity.this, "Error adding to DB", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
    }
}