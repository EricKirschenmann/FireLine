package com.erickirschenmann.fireline.data;

import static com.erickirschenmann.fireline.data.TestUtilities.getStaticIntegerField;
import static com.erickirschenmann.fireline.data.TestUtilities.getStaticStringField;
import static com.erickirschenmann.fireline.data.TestUtilities.studentReadableClassNotFound;
import static com.erickirschenmann.fireline.data.TestUtilities.studentReadableNoSuchField;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * Used to test the database we use in Fireline to cache incident data. Within these tests, we test
 * the following:
 *
 * <p>
 *
 * <p>1) Creation of the database with proper table(s) 2) Insertion of single record into our
 * incident table 3) When a record is already stored in the incident table with a particular date, a
 * new record with the same date will overwrite that record. 4) Verify that NON NULL constraints are
 * working properly on record inserts 5) Verify auto increment is working with the ID 6) Test the
 * onUpgrade functionality of the IncidentDbHelper
 */
@RunWith(AndroidJUnit4.class)
public class TestIncidentDatabase {
  public static final String columnAddressVariableName = "COLUMN_ADDRESS";
  public static final String columnBlockVariableName = "COLUMN_BLOCK";
  public static final String columnCityVariableName = "COLUMN_CITY";
  public static final String columnCommentVariableName = "COLUMN_COMMENT";
  public static final String columnIncidentNumberVariableName = "COLUMN_INCIDENT_NUMBER";
  public static final String columnIncidentTypeVariableName = "COLUMN_INCIDENT_TYPE";
  public static final String columnLatitudeVariableName = "COLUMN_LATITUDE";
  public static final String columnLongitudeVariableName = "COLUMN_LONGITUDE";
  public static final String columnResponseDateVariableName = "COLUMN_RESPONSE_DATE";
  public static final String columnStatusVariableName = "COLUMN_STATUS";
  public static final String columnUnitsVariableName = "COLUMN_UNITS";
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
  private static final String incidentContractName = ".IncidentContract";
  private static final String incidentEntryName = incidentContractName + "$IncidentEntry";
  private static final String incidentDbHelperName = ".IncidentDbHelper";
  private static final String databaseNameVariableName = "DATABASE_NAME";
  private static final String databaseVersionVariableName = "DATABASE_VERSION";
  private static final String tableNameVariableName = "TABLE_NAME";
  private static final String columnDateVariableName = "COLUMN_DATE";
  static String REFLECTED_COLUMN_DATE;
  static String REFLECTED_COLUMN_ADDRESS;
  static String REFLECTED_COLUMN_BLOCK;
  static String REFLECTED_COLUMN_CITY;
  static String REFLECTED_COLUMN_COMMENT;
  static String REFLECTED_COLUMN_INCIDENT_NUMBER;
  static String REFLECTED_COLUMN_INCIDENT_TYPE;
  static String REFLECTED_COLUMN_LATITUDE;
  static String REFLECTED_COLUMN_LONGITUDE;
  static String REFLECTED_COLUMN_RESPONSE_DATE;
  static String REFLECTED_COLUMN_STATUS;
  static String REFLECTED_COLUMN_UNITS;
  private static String REFLECTED_DATABASE_NAME;
  private static int REFLECTED_DATABASE_VERSION;
  private static String REFLECTED_TABLE_NAME;
  /*
   * Context used to perform operations on the database and create IncidentDbHelpers.
   */
  private final Context context = InstrumentationRegistry.getTargetContext();
  private Class incidentEntryClass;
  private Class incidentDbHelperClass;
  private SQLiteDatabase database;
  private SQLiteOpenHelper dbHelper;

  @Before
  public void before() {
    try {
      incidentEntryClass = Class.forName(dataPackageName + incidentEntryName);
      if (!BaseColumns.class.isAssignableFrom(incidentEntryClass)) {
        String weatherEntryDoesNotImplementBaseColumns =
            "IncidentEntry class needs to " + "implement the interface BaseColumns, but does not.";
        fail(weatherEntryDoesNotImplementBaseColumns);
      }

      REFLECTED_TABLE_NAME = getStaticStringField(incidentEntryClass, tableNameVariableName);
      REFLECTED_COLUMN_DATE = getStaticStringField(incidentEntryClass, columnDateVariableName);
      REFLECTED_COLUMN_ADDRESS =
          getStaticStringField(incidentEntryClass, columnAddressVariableName);
      REFLECTED_COLUMN_BLOCK = getStaticStringField(incidentEntryClass, columnBlockVariableName);
      REFLECTED_COLUMN_CITY = getStaticStringField(incidentEntryClass, columnCityVariableName);
      REFLECTED_COLUMN_COMMENT =
          getStaticStringField(incidentEntryClass, columnCommentVariableName);
      REFLECTED_COLUMN_INCIDENT_NUMBER =
          getStaticStringField(incidentEntryClass, columnIncidentNumberVariableName);
      REFLECTED_COLUMN_INCIDENT_TYPE =
          getStaticStringField(incidentEntryClass, columnIncidentTypeVariableName);
      REFLECTED_COLUMN_LATITUDE =
          getStaticStringField(incidentEntryClass, columnLatitudeVariableName);
      REFLECTED_COLUMN_LONGITUDE =
          getStaticStringField(incidentEntryClass, columnLongitudeVariableName);
      REFLECTED_COLUMN_RESPONSE_DATE =
          getStaticStringField(incidentEntryClass, columnResponseDateVariableName);
      REFLECTED_COLUMN_STATUS = getStaticStringField(incidentEntryClass, columnStatusVariableName);
      REFLECTED_COLUMN_UNITS = getStaticStringField(incidentEntryClass, columnUnitsVariableName);

      incidentDbHelperClass = Class.forName(dataPackageName + incidentDbHelperName);
      Class incidentDBHelperSuperClass = incidentDbHelperClass.getSuperclass();

      if (incidentDBHelperSuperClass == null || incidentDBHelperSuperClass.equals(Object.class)) {
        String noExplicitSuperclass =
            "WeatherDbHelper needs to extend SQLiteOpenHelper, but yours currently doesn't extend a class at all.";
        fail(noExplicitSuperclass);
      } else if (incidentDBHelperSuperClass != null) {
        String weatherDbHelperSuperclassName = incidentDBHelperSuperClass.getSimpleName();
        String doesNotExtendOpenHelper =
            "WeatherDbHelper needs to extend SQLiteOpenHelper but yours extends "
                + weatherDbHelperSuperclassName;

        assertTrue(
            doesNotExtendOpenHelper,
            SQLiteOpenHelper.class.isAssignableFrom(incidentDBHelperSuperClass));
      }

      REFLECTED_DATABASE_NAME =
          getStaticStringField(incidentDbHelperClass, databaseNameVariableName);

      REFLECTED_DATABASE_VERSION =
          getStaticIntegerField(incidentDbHelperClass, databaseVersionVariableName);

      Constructor incidentDbHelperClassConstructor =
          incidentDbHelperClass.getConstructor(Context.class);

      dbHelper = (SQLiteOpenHelper) incidentDbHelperClassConstructor.newInstance(context);

      context.deleteDatabase(REFLECTED_DATABASE_NAME);

      Method getWritableDatabase = SQLiteOpenHelper.class.getDeclaredMethod("getWritableDatabase");
      database = (SQLiteDatabase) getWritableDatabase.invoke(dbHelper);

    } catch (ClassNotFoundException e) {
      fail(studentReadableClassNotFound(e));
    } catch (NoSuchFieldException e) {
      fail(studentReadableNoSuchField(e));
    } catch (IllegalAccessException e) {
      fail(e.getMessage());
    } catch (NoSuchMethodException e) {
      fail(e.getMessage());
    } catch (InstantiationException e) {
      fail(e.getMessage());
    } catch (InvocationTargetException e) {
      fail(e.getMessage());
    }
  }
}
