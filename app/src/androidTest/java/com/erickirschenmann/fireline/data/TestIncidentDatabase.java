package com.erickirschenmann.fireline.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import org.junit.Before;

/**
 * Used to test the database we use in Sunshine to cache incident data. Within these tests, we test
 * the following:
 *
 * <p>1) Creation of the database with proper table(s) 2) Insertion of single record into our
 * incident table 3) When a record is already stored in the incident table with a particular date, a
 * new record with the same date will overwrite that record. 4) Verify that NON NULL constraints are
 * working properly on record inserts 5) Verify auto increment is working with the ID 6) Test the
 * onUpgrade functionality of the IncidentDbHelper
 */
public class TestIncidentDatabase {
  /*
   * In order to verify that you have set up your classes properly and followed our TODOs, we
   * need to create what's called a Change Detector Test. In almost any other situation, these
   * tests are discouraged, as they provide no real value in a production setting. However, using
   * reflection to verify that you have set your classes up correctly will help provide more
   * useful errors if you've missed a step in our instructions.
   *
   * Additionally, using reflection for these tests allows you to run the tests when they
   * normally wouldn't compile, as they depend on pieces of your classes that you might not
   * have created when you initially run the tests.
   */
  private static final String packageName = "com.erickirschenmann.fireline";
  private static final String dataPackageName = packageName + ".data";
  private static final String incidentContractName = ".incidentContract";
  private static final String incidentEntryName = incidentContractName + "$IncidentEntry";
  private static final String incidentDbHelperName = ".IncidentDbHelper";
  private static final String databaseNameVariableName = "DATABASE_NAME";
  private static final String tableNameVariableName = "TABLE_NAME";
  private static final String databaseVersionVariableName = "DATABASE_VERSION";
  private static final String columnAddressVariableName = "COLUMN_ADDRESS";
  private static final String columnBlockVariableName = "COLUMN_BLOCK";
  private static final String columnCityVariableName = "COLUMN_CITY";
  private static final String columnCommentVariableName = "COLUMN_COMMENT";
  private static final String columnIncidentNumberVariableName = "COLUMN_INCIDENT_NUMBER";
  private static final String columnIncidentTypeVariableName = "COLUMN_INCIDENT_TYPE";
  private static final String columnLatitudeVariableName = "COLUMN_LATITUDE";
  private static final String columnLongitudeVariableName = "COLUMN_LONGITUDE";
  private static final String columnResponseDateVariableName = "COLUMN_RESPONSE_DATE";
  private static final String columnStatusVariableName = "COLUMN_STATUS";
  private static final String columnUnitsVariableName = "COLUMN_UNITS";
  private static String REFLECTED_DATABASE_NAME;
  private static String REFLECTED_TABLE_NAME;
  private static int REFLECTED_DATABASE_VERSION;
  private static String REFLECTED_COLUMN_ADDRESS;
  private static String REFLECTED_COLUMN_BLOCK;
  private static String REFLECTED_COLUMN_CITY;
  private static String REFLECTED_COLUMN_COMMENT;
  private static String REFLECTED_COLUMN_INCIDENT_NUMBER;
  private static String REFLECTED_COLUMN_INCIDENT_TYPE;
  private static String REFLECTED_COLUMN_LATITUDE;
  private static String REFLECTED_COLUMN_LONGITUDE;
  private static String REFLECTED_COLUMN_RESPONSE_DATE;
  private static String REFLECTED_COLUMN_STATUS;
  private static String REFLECTED_COLUMN_UNITS;
  /*
   * Context used to perform operations on the database and create IncidentDbHelpers.
   */
  private final Context context = InstrumentationRegistry.getTargetContext();
  private Class incidentEntryClass;
  private Class incidentDbHelperClass;
  private SQLiteDatabase database;
  private SQLiteOpenHelper dbHelper;

  @Before
  public void before() {}
}
