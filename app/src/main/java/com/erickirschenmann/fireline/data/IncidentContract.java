package com.erickirschenmann.fireline.data;

import android.provider.BaseColumns;

/** Created by eric on 3/24/17. */
public class IncidentContract {
  public static final class IncidentEntry implements BaseColumns {
    public static final String TABLE_NAME = "incident";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_BLOCK = "block";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_INCIDENT_NUMBER = "incidentNumber";
    public static final String COLUMN_INCIDENT_TYPE = "incidentType";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_RESPONSE_DATE = "responseDate";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_UNITS = "units";
    public static final String COLUMN_DATE = "date";
  }
}
