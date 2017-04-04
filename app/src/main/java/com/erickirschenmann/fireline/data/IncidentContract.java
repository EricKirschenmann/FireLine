package com.erickirschenmann.fireline.data;

import android.provider.BaseColumns;

/** Created by eric on 3/24/17. */
public class IncidentContract {

  /* Inner class that defines the table contents of the incident table */
  public static final class IncidentEntry implements BaseColumns {

    public static final String TABLE_NAME = "incident";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_WEATHER_ID = "incident_id";
    public static final String COLUMN_MIN_TEMP = "min";
    public static final String COLUMN_MAX_TEMP = "max";
    public static final String COLUMN_HUMIDITY = "humidity";
    public static final String COLUMN_PRESSURE = "pressure";
    public static final String COLUMN_WIND_SPEED = "wind";
    public static final String COLUMN_DEGREES = "degrees";
  }
}
