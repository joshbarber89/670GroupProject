package com.example.a670groupproject;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHelperTest {
    private DBHelper dbHelper;
    private SQLiteDatabase mockDatabase;

    @Before
    public void setUp() {
        dbHelper = new DBHelper(mock(Context.class)); // Passing a mock context to avoid actual database operations
        mockDatabase = mock(SQLiteDatabase.class);
        dbHelper.onCreate(mockDatabase); // Manually trigger the onCreate method to create tables in the mock database
    }

    @Test
    public void testInsertData_SuccessfulInsertion() {
        String username = "testUser";
        String password = "testPassword";

        // Assuming a successful insertion
        when(mockDatabase.insert(eq("users"), anyString(), any(ContentValues.class))).thenReturn(1L);

        boolean result = dbHelper.insertData(username, password);

        // Verify that the insert method of the mock database is called once with correct parameters
        verify(mockDatabase, times(1)).insert(eq("users"), anyString(), any(ContentValues.class));
        assertTrue(result); // The result should be true for successful insertion
    }

    @Test
    public void testInsertData_FailedInsertion() {
        String username = "testUser";
        String password = "testPassword";

        // Assuming a failed insertion
        when(mockDatabase.insert(eq("users"), anyString(), any(ContentValues.class))).thenReturn(-1L);

        boolean result = dbHelper.insertData(username, password);

        // Verify that the insert method of the mock database is called once with correct parameters
        verify(mockDatabase, times(1)).insert(eq("users"), anyString(), any(ContentValues.class));
        assertFalse(result); // The result should be false for failed insertion
    }

    @Test
    public void testCheckIfUserExists_UserExists() {
        String userName = "existingUser";

        // Assuming the cursor has some data (indicating the user exists)
        Cursor mockCursor = mock(Cursor.class);
        when(mockCursor.getCount()).thenReturn(1);

        // Assuming the rawQuery method returns the mock cursor
        when(mockDatabase.rawQuery(eq("select * from users where userName = ?"), any(String[].class)))
                .thenReturn(mockCursor);

        boolean result = dbHelper.checkIfUserExists(userName);

        // Verify that the rawQuery method of the mock database is called once with correct parameters
        verify(mockDatabase, times(1)).rawQuery(eq("select * from users where userName = ?"), any(String[].class));
        assertTrue(result); // The result should be true for user exists
    }

    @Test
    public void testCheckIfUserExists_UserDoesNotExist() {
        String userName = "nonExistingUser";

        // Assuming the cursor has no data (indicating the user does not exist)
        Cursor mockCursor = mock(Cursor.class);
        when(mockCursor.getCount()).thenReturn(0);

        // Assuming the rawQuery method returns the mock cursor
        when(mockDatabase.rawQuery(eq("select * from users where userName = ?"), any(String[].class)))
                .thenReturn(mockCursor);

        boolean result = dbHelper.checkIfUserExists(userName);

        // Verify that the rawQuery method of the mock database is called once with correct parameters
        verify(mockDatabase, times(1)).rawQuery(eq("select * from users where userName = ?"), any(String[].class));
        assertFalse(result); // The result should be false for user does not exist
    }
}
