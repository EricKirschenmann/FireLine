package com.erickirschenmann.fireline.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.preference.PreferenceManager;
import com.erickirschenmann.fireline.R;
import com.erickirschenmann.fireline.data.FirelineTestJson;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

/** Created by eric on 3/3/17. */
public class NetworkUtils {

  private static final String TAG = NetworkUtils.class.getSimpleName();
  private static final String FIRELINE_JSON_URL = "http://fireline.ventura.org/data/fireline.json";
  private static final String GOOGLE_JSON_URL =
      "https://maps.googleapis.com/maps/api/geocode/json?address=";

  /**
   * Will return a URL built out of the base Fireline URL which currently does not change but may in
   * the future
   *
   * @return The {@code URL} containing the Fireline address
   */
  public static URL getUrl() {
    Uri firelineUri = Uri.parse(FIRELINE_JSON_URL).buildUpon().build();

    try {
      return new URL(firelineUri.toString());
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static URL getAddressUrl(String address, String apiKey) {
    try {
      String query = URLEncoder.encode(address, "utf-8");
      String url = GOOGLE_JSON_URL + query + "&key=" + apiKey;
      Uri googleUri = Uri.parse(url).buildUpon().build();

      try {
        return new URL(googleUri.toString());
      } catch (MalformedURLException e) {
        e.printStackTrace();
        return null;
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * This method returns the entire result from the HTTP response.
   *
   * @param url The URL to fetch the HTTP response from.
   * @return The contents of the HTTP response.
   * @throws IOException Related to network and stream reading
   */
  public static String getResponseFromHttpUrl(URL url, Context context) throws IOException {
    // use shared preferences to get user's settings about debug data
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    // get the values from resources
    Resources resources = context.getResources();
    String debugKey = resources.getString(R.string.pref_show_debug_key);
    boolean debugDefault = resources.getBoolean(R.bool.pref_show_debug_default);

    // this needs to be separated to it's own method or something but for now it'll only run when needed
    // when I use the user's actual location this will be unnecessary
    // if the location has been changed or does not exist
    if (!sharedPreferences.contains(resources.getString(R.string.pref_location_changed_key))
        || sharedPreferences.getBoolean(
            resources.getString(R.string.pref_location_changed_key),
            resources.getBoolean(R.bool.pref_location_changed_default))) {

      // get the user's latitude and longitude from Google
      String address =
          sharedPreferences.getString(
              resources.getString(R.string.pref_address_key),
              resources.getString(R.string.pref_address_default));
      URL googleURL = getAddressUrl(address, resources.getString(R.string.google_maps_key));

      // get the latitude and longitude from the json
      double[] location = FirelineJsonUtils.getLatLong(getGoogleJsonUrlResponse(googleURL));

      // add the latitude and longitude to the shared preferences
      SharedPreferences.Editor editor = sharedPreferences.edit();
      if (location != null) {
        editor.putFloat(
            resources.getString(R.string.pref_location_latitude_key), (float) location[1]);
        editor.putFloat(
            resources.getString(R.string.pref_location_longitude_key), (float) location[0]);
      }

      // location has been updated
      editor.putBoolean(resources.getString(R.string.pref_location_changed_key), false);
      editor.apply();
    }

    if (sharedPreferences.getBoolean(debugKey, debugDefault)) {
      // instead of constantly downloading from their server, use test data
      return FirelineTestJson.getFIRELINE_TEST_JSON();
    } else {
      HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
      try {
        InputStream in = urlConnection.getInputStream();

        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("\\A");

        boolean hasInput = scanner.hasNext();
        if (hasInput) {
          return scanner.next();
        } else {
          return null;
        }
      } finally {
        urlConnection.disconnect();
      }
    }
  }

  /**
   * Gets a JSON string containing information about an address
   *
   * @param url The url containing the address and the api key
   * @return A string of JSON containing the data
   * @throws IOException Related to network and stream reading
   */
  private static String getGoogleJsonUrlResponse(URL url) throws IOException {
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    try {
      InputStream in = urlConnection.getInputStream();

      Scanner scanner = new Scanner(in);
      scanner.useDelimiter("\\A");

      boolean hasInput = scanner.hasNext();
      if (hasInput) {
        return scanner.next();
      } else {
        return null;
      }
    } finally {
      urlConnection.disconnect();
    }
  }
}
