package com.example.a670groupproject;

import android.content.SharedPreferences;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a670groupproject.DBHelper;
import com.example.a670groupproject.LoginActivity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static android.content.Context.MODE_PRIVATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoginActivityTest {
    @Mock
    private SharedPreferences mockLoginPrefs;
    @Mock
    private SharedPreferences.Editor mockEditor;
    @Mock
    private SharedPreferences mockUserPrefs;
    @Mock
    private SharedPreferences.Editor mockUserEditor;
    @Mock
    private SharedPreferences mockSpecificUserPrefs;
    @Mock
    private SharedPreferences.Editor mockSpecificUserEditor;

    @Mock
    private DBHelper mockDB;

    private Toast mockToast = mock(Toast.class);

    private LoginActivity loginActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        loginActivity = Mockito.spy(new LoginActivity());
        doNothing().when(loginActivity).startActivity(any());
        doReturn(mockDB).when(loginActivity).getDBHelper();

        // Mock the SharedPreferences behavior
        when(loginActivity.getSharedPreferences("loginPreferences", MODE_PRIVATE)).thenReturn(mockLoginPrefs);
        when(mockLoginPrefs.edit()).thenReturn(mockEditor);
        when(mockLoginPrefs.getString("rememberMe", "false")).thenReturn("false");
        when(mockLoginPrefs.getString("loggedOut", "false")).thenReturn("false");

        // Mock the specific user preferences
        when(loginActivity.getSharedPreferences(any(), anyInt())).thenReturn(mockSpecificUserPrefs);
        when(mockSpecificUserPrefs.edit()).thenReturn(mockSpecificUserEditor);
        when(mockSpecificUserPrefs.getInt("login_count", 0)).thenReturn(0);
    }

    @Test
    public void testLoadUserData_RememberMeChecked() {
        // Simulate Remember Me checked in UI
        when(mockLoginPrefs.getString("rememberMe", "false")).thenReturn("true");
        when(mockLoginPrefs.getString("email", "email@domain.com")).thenReturn("test@example.com");
        when(mockLoginPrefs.getString("password", "")).thenReturn("password123");

        CheckBox rememberMeCheckBox = Mockito.mock(CheckBox.class);
        when(loginActivity.findViewById(R.id.loginActivityCheckboxRememberMe)).thenReturn(rememberMeCheckBox);

        // Call the method to test
        loginActivity.loadUserData();

        // Verify that the methods were called with the correct parameters
        verify(rememberMeCheckBox).setChecked(true);
        verify(loginActivity).loginProcess("test@example.com", "password123");
    }

    @Test
    public void testLoadUserData_LoggedOut() {
        // Simulate Remember Me unchecked and logged out in UI
        when(mockLoginPrefs.getString("rememberMe", "false")).thenReturn("false");
        when(mockLoginPrefs.getString("loggedOut", "false")).thenReturn("true");
        when(mockLoginPrefs.getString("email", "email@domain.com")).thenReturn("test@example.com");
        when(mockLoginPrefs.getString("password", "")).thenReturn("password123");

        EditText emailEditText = Mockito.mock(EditText.class);
        EditText passwordEditText = Mockito.mock(EditText.class);
        when(loginActivity.findViewById(R.id.loginActivityEditTextEmailAddress)).thenReturn(emailEditText);
        when(loginActivity.findViewById(R.id.loginActivityEditTextPassword)).thenReturn(passwordEditText);

        // Call the method to test
        loginActivity.loadUserData();

        // Verify that the methods were called with the correct parameters
        verify(emailEditText).setText("test@example.com");
        verify(passwordEditText).setText("password123");
    }

    @Test
    public void testLoginProcess_SuccessfulLogin() {
        // Simulate successful login in DBHelper
        when(mockDB.checkUsernameAndPassword("test@example.com", "password123")).thenReturn(true);

        // Mock the SharedPreferences behavior for global user preferences
        when(loginActivity.getSharedPreferences("globalUserPreferences", MODE_PRIVATE)).thenReturn(mockUserPrefs);
        when(mockUserPrefs.edit()).thenReturn(mockUserEditor);

        // Mock the specific user preferences
        when(mockSpecificUserPrefs.getInt("login_count", 0)).thenReturn(0);

        // Call the method to test
        loginActivity.loginProcess("test@example.com", "password123");

        // Verify that the expected intents and preferences were set
        verify(loginActivity).startActivity(any());
        verify(mockUserEditor).putString("emailAddress", "test@example.com");
        verify(mockUserEditor).apply();
        verify(mockSpecificUserEditor).putInt("login_count", 1);
        verify(mockSpecificUserEditor).apply();
    }

    @Test
    public void testLoginProcess_IncorrectLogin() {
        // Simulate incorrect login in DBHelper
        when(LoginActivity.getDBHelper().checkUsernameAndPassword("incorrect@example.com", "wrongpassword")).thenReturn(false);

        // Mock the SharedPreferences behavior for global user preferences
        when(loginActivity.getSharedPreferences("globalUserPreferences", MODE_PRIVATE)).thenReturn(mockUserPrefs);
        when(mockUserPrefs.edit()).thenReturn(mockUserEditor);

        // Mock the Toast and Toast.makeText
        when(Toast.makeText(loginActivity.getApplicationContext(), "Incorrect login", Toast.LENGTH_SHORT)).thenReturn(mockToast);

        // Call the method to test
        loginActivity.loginProcess("incorrect@example.com", "wrongpassword");

        // Verify that the expected Toast message is shown
        verify(mockToast).show();
        // Verify that the correct intents and preferences were NOT set
        verify(loginActivity, never()).startActivity(any());
        verify(mockUserEditor, never()).putString(eq("emailAddress"), anyString());
        verify(mockUserEditor, never()).apply();
        verify(mockSpecificUserEditor, never()).putInt(eq("login_count"), anyInt());
        verify(mockSpecificUserEditor, never()).apply();
    }

}
