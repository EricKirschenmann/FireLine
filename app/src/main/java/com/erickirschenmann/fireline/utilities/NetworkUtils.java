package com.erickirschenmann.fireline.utilities;

import android.net.Uri;
import android.util.Log;
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
  public static String getResponseFromHttpUrl(URL url) throws IOException {
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
