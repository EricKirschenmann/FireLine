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
    String address;

    // if the address is not the default
    if (sharedPreferences.contains(context.getString(R.string.pref_address_key))) {
      address =
          sharedPreferences.getString(
              context.getString(R.string.pref_address_key),
              context.getString(R.string.pref_address_default));
    } else {
      address = context.getString(R.string.pref_address_test_default);
    }

    // hacky way of using the default address, crashes the app using default distance
    if (address.equals("165 Durley Ave., Camarillo, CA 93010")) {
      return new LatLng(34.209056, -119.074665);
    }

    try {
      // attempt to use the user's address
      return LocationUtils.getLocationFromAddress(context, address);
    } catch (IndexOutOfBoundsException e) {
      // will alert the user it failed
      SharedPreferences.Editor editor = sharedPreferences.edit();
      editor.putBoolean(context.getString(R.string.pref_location_failed_key), true);
      editor.apply();
      // use default location
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
}
