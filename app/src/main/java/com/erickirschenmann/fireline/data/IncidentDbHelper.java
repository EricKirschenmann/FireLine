package com.erickirschenmann.fireline.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.erickirschenmann.fireline.data.IncidentContract.IncidentEntry;

/** Created by eric on 3/24/17. */
public class IncidentDbHelper extends SQLiteOpenHelper {

  // the name of the actual database file on the system
  public static final String DATABASE_NAME = "incident.db";
  // the current database version
  public static final int DATABASE_VERSION = 1;

  /**
   * Default constructor just calls the constructor for the super class
   *
   * @param context The context creating the database
   */
  public IncidentDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    final String SQL_CREATE_INCIDENT_TABLE =
        "CREATE TABLE "
            + IncidentEntry.TABLE_NAME
            + " ("
            + IncidentEntry._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + IncidentEntry.COLUMN_ADDRESS
            + " TEXT NOT NULL, "
            + IncidentEntry.COLUMN_BLOCK
            + " TEXT NOT NULL, "
            + IncidentEntry.COLUMN_CITY
            + " TEXT NOT NULL, "
            + IncidentEntry.COLUMN_COMMENT
            + "TEXT NOT NULL, "
            + IncidentEntry.COLUMN_INCIDENT_NUMBER
            + " TEXT NOT NULL, "
            + IncidentEntry.COLUMN_INCIDENT_TYPE
            + " TEXT NOT NULL, "
            + IncidentEntry.COLUMN_LATITUDE
            + " REAL NOT NULL, "
            + IncidentEntry.COLUMN_LONGITUDE
            + " REAL NOT NULL, "
            + IncidentEntry.COLUMN_RESPONSE_DATE
            + " TEXT NOT NULL, "
            + IncidentEntry.COLUMN_STATUS
            + " TEXT NOT NULL, "
            + IncidentEntry.COLUMN_UNITS
            + " TEXT NOT NULL"
            + ");";
    db.execSQL(SQL_CREATE_INCIDENT_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + IncidentEntry.TABLE_NAME);
    onCreate(db);
  }
}
