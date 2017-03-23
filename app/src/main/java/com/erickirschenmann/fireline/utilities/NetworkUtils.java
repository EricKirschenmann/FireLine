package com.erickirschenmann.fireline.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import com.erickirschenmann.fireline.R;
import com.erickirschenmann.fireline.data.FirelineTestJson;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/** Created by eric on 3/3/17. */
public class NetworkUtils {

  private static final String TAG = NetworkUtils.class.getSimpleName();
  private static final String FIRELINE_JSON_URL = "http://fireline.ventura.org/data/fireline.json";

  /**
   * Will return a URL built out of the base Fireline URL which currently does not change but may in
   * the future
   *
   * @return The {@code URL} containing the Fireline address
   */
  public static URL getUrl() {
    Uri firelineUri = Uri.parse(FIRELINE_JSON_URL).buildUpon().build();
    Log.v(TAG, "Built Uri: " + firelineUri.toString());

    try {
      return new URL(firelineUri.toString());
    } catch (MalformedURLException e) {
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
}
