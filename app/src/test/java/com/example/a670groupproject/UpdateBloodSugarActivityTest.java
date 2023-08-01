package com.example.a670groupproject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class UpdateBloodSugarActivityTest {

    private DBHelper mockDB;
    private UpdateBloodSugarHelper updateBloodSugarHelper;

    @Before
    public void setUp() {
        mockDB = mock(DBHelper.class);
        updateBloodSugarHelper = new UpdateBloodSugarHelper();
        updateBloodSugarHelper.DB = mockDB;
    }

    @Test
    public void testUpdateBloodSugar_ValidInput_mmolPerLitre() {
        String entryID = "1";
        String day = "15";
        String month = "09";
        String year = "2023";
        String hour = "10";
        String minute = "30";
        String value = "6.0";
        String amPMValue = "AM";

        // Set the selected blood sugar unit to mmol/L
        MainActivity.selectedBloodSugarUnit = SettingsActivity.BloodSugarUnit_mmolPerLitre;

        assertTrue(updateBloodSugarHelper.update(entryID, day, month, year, hour, minute, value, amPMValue));

        // Verify that the correct updateEntry methods are called with the right parameters
        verify(mockDB, times(1)).updateEntry("bloodSugarTable0", entryID, value, day, month, year, hour, minute, amPMValue);
        verify(mockDB, times(1)).updateEntry("bloodSugarTable1", entryID, "108.108", day, month, year, hour, minute, amPMValue);
    }

    @Test
    public void testUpdateBloodSugar_ValidInput_mgPerDeciLitre() {
        String entryID = "1";
        String day = "15";
        String month = "09";
        String year = "2023";
        String hour = "10";
        String minute = "30";
        String value = "108.108";
        String amPMValue = "AM";

        // Set the selected blood sugar unit to mg/dL
        MainActivity.selectedBloodSugarUnit = SettingsActivity.BloodSugarUnit_mgPerDeciLitre;

        assertTrue(updateBloodSugarHelper.update(entryID, day, month, year, hour, minute, value, amPMValue));

        // Verify that the correct updateEntry methods are called with the right parameters
        verify(mockDB, times(1)).updateEntry("bloodSugarTable0", entryID, "6.0", day, month, year, hour, minute, amPMValue);
        verify(mockDB, times(1)).updateEntry("bloodSugarTable1", entryID, value, day, month, year, hour, minute, amPMValue);
    }

    @Test
    public void testUpdateBloodSugar_InvalidInput() {
        String entryID = "1";
        String day = "35"; // Invalid day
        String month = "13"; // Invalid month
        String year = "2023";
        String hour = "24"; // Invalid hour
        String minute = "70"; // Invalid minute
        String value = "abc"; // Invalid value
        String amPMValue = "PM"; // Invalid AM/PM value

        assertFalse(updateBloodSugarHelper.update(entryID, day, month, year, hour, minute, value, amPMValue));

        // Verify that no updateEntry methods are called (since the input is invalid)
        verify(mockDB, never()).updateEntry(any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testDeleteBloodSugar() {
        String entryID = "1";

        assertTrue(updateBloodSugarHelper.delete(entryID));

        // Verify that the correct deleteEntry methods are called with the right parameters
        verify(mockDB, times(1)).deleteEntry("bloodSugarTable0", entryID);
        verify(mockDB, times(1)).deleteEntry("bloodSugarTable1", entryID);
    }
}