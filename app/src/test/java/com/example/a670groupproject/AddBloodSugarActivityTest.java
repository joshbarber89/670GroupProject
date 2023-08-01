package com.example.a670groupproject;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AddBloodSugarActivityTest {
    private DBHelper mockDB;
    private AddBloodSugarHelper addBloodSugarHelper;

    @Before
    public void setUp() {
        mockDB = mock(DBHelper.class);
        addBloodSugarHelper = new AddBloodSugarHelper();
        addBloodSugarHelper.DB = mockDB;
    }

    @Test
    public void testInsert_ValidInput_mmolPerLitre() {
        String day = "15";
        String month = "09";
        String year = "2023";
        String hour = "10";
        String minute = "30";
        String value = "6.5"; // Some valid value
        String amPMValue = "AM";

        // Set the selected blood sugar unit to mmolPerLitre
        MainActivity.selectedBloodSugarUnit = SettingsActivity.BloodSugarUnit_mmolPerLitre;

        assertTrue(addBloodSugarHelper.insert(day, month, year, hour, minute, value, amPMValue));

        // Verify that the correct insertEntry method is called with the right parameters
        verify(mockDB, times(1)).insertEntry("bloodSugarTable0", value, day, month, year, hour, minute, amPMValue);
        verify(mockDB, times(1)).insertEntry("bloodSugarTable1", (Float.parseFloat(value) * 18.018) + "", day, month, year, hour, minute, amPMValue);
    }

    @Test
    public void testInsert_ValidInput_mgPerDeciLitre() {
        String day = "15";
        String month = "09";
        String year = "2023";
        String hour = "10";
        String minute = "30";
        String value = "120"; // Some valid value
        String amPMValue = "AM";

        // Set the selected blood sugar unit to mgPerDeciLitre
        MainActivity.selectedBloodSugarUnit = SettingsActivity.BloodSugarUnit_mgPerDeciLitre;

        assertTrue(addBloodSugarHelper.insert(day, month, year, hour, minute, value, amPMValue));

        // Verify that the correct insertEntry method is called with the right parameters
        verify(mockDB, times(1)).insertEntry("bloodSugarTable1", value, day, month, year, hour, minute, amPMValue);
        verify(mockDB, times(1)).insertEntry("bloodSugarTable0", (Float.parseFloat(value) * 0.0555) + "", day, month, year, hour, minute, amPMValue);
    }

    @Test
    public void testInsert_InvalidInput() {
        String day = "35"; // Invalid day
        String month = "13"; // Invalid month
        String year = "2023";
        String hour = "24"; // Invalid hour
        String minute = "70"; // Invalid minute
        String value = "abc"; // Invalid value
        String amPMValue = "PM"; // Invalid AM/PM value

        assertFalse(addBloodSugarHelper.insert(day, month, year, hour, minute, value, amPMValue));

        // Verify that no insertEntry method is called (since the input is invalid)
        verify(mockDB, never()).insertEntry(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testDatetimeModification_SingleDigit() {
        int value = 5;
        String expectedOutput = "05";

        String result = addBloodSugarHelper.datetimeModification(value);

        assertTrue(result.equals(expectedOutput));
    }

    @Test
    public void testDatetimeModification_DoubleDigit() {
        int value = 12;
        String expectedOutput = "12";

        String result = addBloodSugarHelper.datetimeModification(value);

        assertTrue(result.equals(expectedOutput));
    }
}
