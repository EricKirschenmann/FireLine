package com.erickirschenmann.fireline.data;

import android.support.test.runner.AndroidJUnit4;
import org.junit.runner.RunWith;

/**
 * Used to test the database we use in Sunshine to cache weather data. Within these tests, we
 * test the following:
 * <p>
 * <p>
 * 1) Creation of the database with proper table(s)
 * 2) Insertion of single record into our weather table
 * 3) When a record is already stored in the weather table with a particular date, a new record
 * with the same date will overwrite that record.
 * 4) Verify that NON NULL constraints are working properly on record inserts
 * 5) Verify auto increment is working with the ID
 * 6) Test the onUpgrade functionality of the WeatherDbHelper
 */
@RunWith(AndroidJUnit4.class)

public class TestIncidentDatabase {
}
