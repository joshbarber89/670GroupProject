package com.example.a670groupproject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DBHelperTest {
    private DBHelper dbHelper;

    @Mock
    private SQLiteDatabase mockDB;

    @Mock
    private Context mockContext;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create the DBHelper instance with the mock SQLiteDatabase
        dbHelper = new DBHelper(mockContext);


        // Mock the onCreate method to prevent executing actual SQL
        doNothing().when(mockDB).execSQL(any());
    }

    @Test
    public void testInsertData_ValidInput() {
        String username = "testuser";
        String password = "testpassword";

        // Mock the insert method to return a valid result
        when(mockDB.insert(eq("users"), any(), any(ContentValues.class))).thenReturn(1L);

        assertTrue(dbHelper.insertData(username, password));

        // Verify that the correct insert method is called with the right parameters
        ContentValues contentValues = new ContentValues();
        contentValues.put("userName", username);
        contentValues.put("password", password);
        verify(mockDB, times(1)).insert("users", null, contentValues);
    }

    @Test
    public void testInsertData_InvalidInput() {
        // Mock the insert method to return an invalid result
        when(mockDB.insert(eq("users"), any(), any(ContentValues.class))).thenReturn(-1L);

        String username = "testuser";
        String password = "testpassword";

        assertFalse(dbHelper.insertData(username, password));

        // Verify that the correct insert method is called with the right parameters
        ContentValues contentValues = new ContentValues();
        contentValues.put("userName", username);
        contentValues.put("password", password);
        verify(mockDB, times(1)).insert("users", null, contentValues);
    }

}
