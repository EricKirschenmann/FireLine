package com.erickirschenmann.fireline.data;

import android.provider.BaseColumns;

/** Created by eric on 3/24/17. */
public class WeatherContract {

  // COMPLETED (1) Within WeatherContract, create a public static final class called WeatherEntry that implements BaseColumns
  /* Inner class that defines the table contents of the incident table */
  public static final class WeatherEntry implements BaseColumns {
    // Do steps 2 through 10 within the WeatherEntry class

    // COMPLETED (2) Create a public static final String call TABLE_NAME with the value "weather"
    public static final String TABLE_NAME = "weather";

    // COMPLETED (3) Create a public static final String call COLUMN_DATE with the value "date"
    public static final String COLUMN_DATE = "date";

    // COMPLETED (4) Create a public static final String call COLUMN_WEATHER_ID with the value "weather_id"
    public static final String COLUMN_WEATHER_ID = "weather_id";

    // COMPLETED (5) Create a public static final String call COLUMN_MIN_TEMP with the value "min"
    public static final String COLUMN_MIN_TEMP = "min";

    // COMPLETED (6) Create a public static final String call COLUMN_MAX_TEMP with the value "max"
    public static final String COLUMN_MAX_TEMP = "max";

    // COMPLETED (7) Create a public static final String call COLUMN_HUMIDITY with the value "humidity"
    public static final String COLUMN_HUMIDITY = "humidity";

    // COMPLETED (8) Create a public static final String call COLUMN_PRESSURE with the value "pressure"
    public static final String COLUMN_PRESSURE = "pressure";

    // COMPLETED (9) Create a public static final String call COLUMN_WIND_SPEED with the value "wind"
    public static final String COLUMN_WIND_SPEED = "wind";

    // COMPLETED (10) Create a public static final String call COLUMN_DEGREES with the value "degrees"
    public static final String COLUMN_DEGREES = "degrees";
  }
}
