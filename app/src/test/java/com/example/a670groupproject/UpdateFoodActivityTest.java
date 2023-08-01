package com.example.a670groupproject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class UpdateFoodActivityTest {
    private DBHelper mockDB;
    private UpdateFoodHelper updateFoodHelper;

    @Before
    public void setUp() {
        mockDB = mock(DBHelper.class);
        updateFoodHelper = new UpdateFoodHelper();
        updateFoodHelper.DB = mockDB;
    }

    @Test
    public void testUpdate_ValidInput() {
        String entryID = "1";
        String day = "15";
        String month = "09";
        String year = "2023";
        String hour = "10";
        String minute = "30";
        String value = "500"; // Some valid value
        String amPMValue = "AM";

        assertTrue(updateFoodHelper.update(entryID, day, month, year, hour, minute, value, amPMValue));

        // Verify that the correct updateEntry method is called with the right parameters
        verify(mockDB, times(1)).updateEntry("foodTable", entryID, value, day, month, year, hour, minute, amPMValue);
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

        assertFalse(updateFoodHelper.update(entryID, day, month, year, hour, minute, value, amPMValue));

        // Verify that no updateEntry method is called (since the input is invalid)
        verify(mockDB, never()).updateEntry(any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testDelete() {
        String entryID = "1";

        assertTrue(updateFoodHelper.delete(entryID));

        // Verify that the correct deleteEntry method is called with the right parameters
        verify(mockDB, times(1)).deleteEntry("foodTable", entryID);
    }
}
