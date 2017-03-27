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
  private static final int DATABASE_VERSION = 3;

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
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + IncidentEntry.COLUMN_DATE
            + " INTEGER NOT NULL, "
            + IncidentEntry.COLUMN_INCIDENT_ID
            + " INTEGER NOT NULL, "
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
            + " UNIQUE ("
            + IncidentEntry.COLUMN_DATE
            + ") ON CONFLICT REPLACE);";
    db.execSQL(SQL_CREATE_INCIDENT_TABLE);
  }

  /**
   * This database is only a cache for online data, so its upgrade policy is simply to discard the
   * data and call through to onCreate to recreate the table. Note that this only fires if you
   * change the version number for your database (in our case, DATABASE_VERSION). It does NOT depend
   * on the version number for your application found in your app/build.gradle file. If you want to
   * update the schema without wiping data, commenting out the current body of this method should be
   * your top priority before modifying this method.
   *
   * @param db Database that is being upgraded
   * @param oldVersion The old database version
   * @param newVersion The new database version
   */
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + IncidentEntry.TABLE_NAME);
    onCreate(db);
  }
}
