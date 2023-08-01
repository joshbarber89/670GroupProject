package com.example.a670groupproject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class UpdateInsulinActivityTest {
    private DBHelper mockDB;
    private UpdateInsulinHelper updateInsulinHelper;

    @Before
    public void setUp() {
        mockDB = mock(DBHelper.class);
        updateInsulinHelper = new UpdateInsulinHelper();
        updateInsulinHelper.DB = mockDB;
    }

    @Test
    public void testUpdate_ValidInput_UIUPerMilliLitre() {
        String entryID = "1";
        String day = "15";
        String month = "09";
        String year = "2023";
        String hour = "10";
        String minute = "30";
        String value = "50"; // Some valid value
        String amPMValue = "AM";

        // Set selectedBloodInsulinUnit to uIUPerMilliLitre (1)
        MainActivity.selectedBloodInsulinUnit = SettingsActivity.BloodInsulinUnit_uIUPerMilliLitre;

        assertTrue(updateInsulinHelper.update(entryID, day, month, year, hour, minute, value, amPMValue));

        // Verify that the correct updateEntry method is called with the right parameters
        verify(mockDB, times(1)).updateEntry("insulinTable0", entryID, value, day, month, year, hour, minute, amPMValue);
        verify(mockDB, times(1)).updateEntry("insulinTable1", entryID, (Float.parseFloat(value) * 6) + "", day, month, year, hour, minute, amPMValue);
    }

    @Test
    public void testUpdate_ValidInput_PMOLPerLitre() {
        String entryID = "1";
        String day = "15";
        String month = "09";
        String year = "2023";
        String hour = "10";
        String minute = "30";
        String value = "30"; // Some valid value
        String amPMValue = "AM";

        // Set selectedBloodInsulinUnit to MillilitrePerGram (0)
        MainActivity.selectedBloodInsulinUnit = SettingsActivity.BloodInsulinUnit_pmolPerLitre;

        assertTrue(updateInsulinHelper.update(entryID, day, month, year, hour, minute, value, amPMValue));

        // Verify that the correct updateEntry method is called with the right parameters
        verify(mockDB, times(1)).updateEntry("insulinTable0", entryID, (Float.parseFloat(value) / 6) + "", day, month, year, hour, minute, amPMValue);
        verify(mockDB, times(1)).updateEntry("insulinTable1", entryID, value, day, month, year, hour, minute, amPMValue);
    }

    @Test
    public void testUpdate_InvalidInput() {
        String entryID = "1";
        String day = "35"; // Invalid day
        String month = "13"; // Invalid month
        String year = "2023";
        String hour = "24"; // Invalid hour
        String minute = "70"; // Invalid minute
        String value = "abc"; // Invalid value
        String amPMValue = "PM"; // Invalid AM/PM value

        assertFalse(updateInsulinHelper.update(entryID, day, month, year, hour, minute, value, amPMValue));

        // Verify that no updateEntry method is called (since the input is invalid)
        verify(mockDB, never()).updateEntry(any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testDelete() {
        String entryID = "1";

        assertTrue(updateInsulinHelper.delete(entryID));

        // Verify that the correct deleteEntry method is called with the right parameters
        verify(mockDB, times(1)).deleteEntry("insulinTable0", entryID);
        verify(mockDB, times(1)).deleteEntry("insulinTable1", entryID);
    }
}
