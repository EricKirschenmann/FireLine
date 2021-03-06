package com.erickirschenmann.fireline.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import com.erickirschenmann.fireline.R;
import com.erickirschenmann.fireline.models.Incident;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/** Created by eric on 3/4/17. */
public class FirelineJsonUtils {

  // will contain the current Incident objects
  // private ArrayList<Incident> incidents;

  public static ArrayList<Incident> getIncidentsFromJson(Context context, String json) {

    ArrayList<Incident> incidents = new ArrayList<>();
    LatLng userLocation = getUserLatLng(context);

    try {
      // create an array of json objects
      JSONArray jsonArray = new JSONArray(json);

      // iterate through the array and retrieve information and create Incident objects
      for (int x = 0; x < jsonArray.length(); x++) {
        // current incident
        JSONObject current = jsonArray.getJSONObject(x);

        // a ton of data but hopefully useful later on
        String address = current.getString("Address");
        String block = current.getString("Block");
        String city = current.getString("City");
        String comment = current.getString("Comment");
        String incidentNumber = current.getString("IncidentNumber");
        String incidentType = current.getString("IncidentType");
        double latitude = current.getDouble("Latitude");
        double longitude = current.getDouble("Longitude");
        String responseDate = current.getString("ResponseDate");
        String status = current.getString("Status");
        String units = current.getString("Units");

        LatLng incidentLocation = new LatLng(latitude, longitude);

        // get distance between points
        double distance = getDistance(incidentLocation, userLocation);

        // create and add a new Incident Object to the ArrayList
        Incident incident =
            new Incident(
                address,
                block,
                city,
                comment,
                incidentNumber,
                incidentType,
                latitude,
                longitude,
                responseDate,
                status,
                units,
                incidentLocation,
                distance);
        incidents.add(incident);
      }

    } catch (JSONException e) {
      e.printStackTrace();
    }

    return incidents;
  }

  private static LatLng getUserLatLng(Context context) {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    if (sharedPreferences.contains(context.getString(R.string.pref_location_latitude_key))
        && sharedPreferences.contains(context.getString(R.string.pref_location_longitude_key))) {
      double latitude =
          (double)
              sharedPreferences.getFloat(
                  context.getString(R.string.pref_location_latitude_key),
                  Float.parseFloat(context.getString(R.string.pref_location_latitude_default)));
      double longitude =
          (double)
              sharedPreferences.getFloat(
                  context.getString(R.string.pref_location_longitude_key),
                  Float.parseFloat(context.getString(R.string.pref_location_longitude_default)));
      return new LatLng(latitude, longitude);
    } else {
      return new LatLng(34.209056, -119.074665);
    }
  }

  private static double getDistance(LatLng incidentLocation, LatLng userLocation) {
    double startLatitude = incidentLocation.latitude;
    double startLongitude = incidentLocation.longitude;
    double endLatitude = userLocation.latitude;
    double endLongitude = userLocation.longitude;

    float[] results = new float[5];

    try {
      Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }

    // distance in miles
    return (double) results[0] / 1609.34;
  }

  static double[] getLatLong(String json) {

    double[] location = new double[2];

    try {
      JSONObject jsonObject = new JSONObject(json);

      location[0] =
          ((JSONArray) jsonObject.get("results"))
              .getJSONObject(0)
              .getJSONObject("geometry")
              .getJSONObject("location")
              .getDouble("lng");

      location[1] =
          ((JSONArray) jsonObject.get("results"))
              .getJSONObject(0)
              .getJSONObject("geometry")
              .getJSONObject("location")
              .getDouble("lat");

    } catch (JSONException e) {
      return null;
    }

    return location;
  }
}
