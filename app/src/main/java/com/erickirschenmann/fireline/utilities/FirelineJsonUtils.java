package com.erickirschenmann.fireline.utilities;

import com.erickirschenmann.fireline.models.Incident;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/** Created by eric on 3/4/17. */
public class FirelineJsonUtils {

  // will contain the current Incident objects
  private ArrayList<Incident> incidents;

  String[] getIncidentsFromJson(String json) {

    String[] formattedIncidents;
    incidents = new ArrayList<>();

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
                units);
        this.incidents.add(incident);
      }

    } catch (JSONException e) {
      e.printStackTrace();
    }

    // using the incident objects get the toString()
    formattedIncidents = new String[this.incidents.size()];
    for (int x = 0; x < formattedIncidents.length; x++) {
      formattedIncidents[x] = this.incidents.get(x).toString();
    }

    return formattedIncidents;
  }
}
