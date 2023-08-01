package com.example.a670groupproject;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class RegisterActivityTest {
    private RegisterHelper registerHelper;
    private DBHelper mockDB;

    @Before
    public void setUp() {
        registerHelper = new RegisterHelper();
        mockDB = mock(DBHelper.class);
        registerHelper.DB = mockDB;
    }

    @Test
    public void testValidateRegistration_InvalidRegistration_UserAlreadyExists() {
        String emailAddress = "existing_user@example.com";
        String password = "password123";
        String rePassword = "password123";

        // Assuming DB.checkIfUserExists(emailAddress) returns true (user already exists)
        when(mockDB.checkIfUserExists(emailAddress)).thenReturn(true);

        int result = registerHelper.validateRegistration(emailAddress, password, rePassword);

        // Verify that DB.checkIfUserExists(emailAddress) is called once
        verify(mockDB, times(1)).checkIfUserExists(emailAddress);
        // Verify that DB.insertData(emailAddress, password) is not called
        verify(mockDB, never()).insertData(anyString(), anyString());
        // Verify that the result is as expected (6 for user already exists)
        assert result == 6;
    }

    @Test
    public void testValidateRegistration_ValidRegistration() {
        String emailAddress = "new_user@example.com";
        String password = "password123";
        String rePassword = "password123";

        // Assuming DB.checkIfUserExists(emailAddress) returns false (user does not exist)
        when(mockDB.checkIfUserExists(emailAddress)).thenReturn(false);

        // Assuming DB.insertData(emailAddress, password) returns true (insertion success)
        when(mockDB.insertData(emailAddress, password)).thenReturn(true);

        int result = registerHelper.validateRegistration(emailAddress, password, rePassword);

        // Verify that DB.checkIfUserExists(emailAddress) is called once
        verify(mockDB, times(1)).checkIfUserExists(emailAddress);
        // Verify that DB.insertData(emailAddress, password) is called once
        verify(mockDB, times(1)).insertData(emailAddress, password);
        // Verify that the result is as expected (1 for successful registration)
        assert result == 1;
    }

}
