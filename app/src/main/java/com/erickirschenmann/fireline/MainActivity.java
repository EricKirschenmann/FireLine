package com.erickirschenmann.fireline;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.erickirschenmann.fireline.IncidentAdapter.IncidentAdapterOnClickHandler;
import com.erickirschenmann.fireline.models.Incident;
import com.erickirschenmann.fireline.utilities.FirelineJsonUtils;
import com.erickirschenmann.fireline.utilities.NetworkUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

// TODO (1) Implement the proper LoaderCallbacks interface and the methods of that interface
public class MainActivity extends AppCompatActivity implements IncidentAdapterOnClickHandler {

  private ArrayList<Incident> incidents;

  private RecyclerView mRecyclerView;
  private IncidentAdapter mIncidentAdapter;
  private TextView mErrorMessageTextView;
  private ProgressBar mProgressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // allows us to manipulate the RecyclerView based on this reference
    mRecyclerView = (RecyclerView) findViewById(R.id.rv_incident);

    mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);

    // specifies the direction of the layout as VERTICAL as opposed to HORIZONTAL
    LinearLayoutManager layoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

    mRecyclerView.setLayoutManager(layoutManager);

    // all of the items are the same size
    mRecyclerView.setHasFixedSize(true);

    // responsible for linking the data to the ViewHolders
    mIncidentAdapter = new IncidentAdapter(this);

    mRecyclerView.setAdapter(mIncidentAdapter);

    mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

    // TODO (7) Remove the code for the AsyncTask and initialize the AsyncTaskLoader
    // initial load of data
    loadEmergencyData();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    // TODO (5) Refactor the refresh functionality to work with our AsyncTaskLoader
    // check which item was selected
    switch (item.getItemId()) {
      case R.id.action_refresh:
        // when the refresh selected refresh the data
        mIncidentAdapter.setIncidentData(null);
        loadEmergencyData();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  /**
   * Will execute the {@link FetchEmergencyTask} AsyncTask using the URL provided by {@link
   * NetworkUtils}
   */
  void loadEmergencyData() {
    new FetchEmergencyTask().execute(NetworkUtils.getUrl());
  }

  // TODO (2) Within onCreateLoader, return a new AsyncTaskLoader that looks a lot like the existing FetchWeatherTask.
  // TODO (3) Cache the weather data in a member variable and deliver it in onStartLoading.

  // TODO (4) When the load is finished, show either the data or an error message if there is no data

  /**
   * Handles the click on one of the RecyclerView items
   *
   * @param incidentData The Google Maps intent location of the Incident
   */
  @Override
  public void onClick(String incidentData) {
    // convert to a valid Uri
    Uri gmmIntentUri = Uri.parse(incidentData);
    // create the intent
    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
    // start the Google Maps activity
    mapIntent.setPackage("com.google.android.apps.maps");
    startActivity(mapIntent);
  }

  /**
   * This method will make the View for the JSON data visible and hide the error message. Since it
   * is okay to redundantly set the visibility of a View, we don't need to check whether each view
   * is currently visible or invisible.
   */
  void showEmergencyData() {
    mRecyclerView.setVisibility(View.VISIBLE);
    mErrorMessageTextView.setVisibility(View.INVISIBLE);
  }

  /**
   * This method will make the error message visible and hide the JSON View. Since it is okay to
   * redundantly set the visibility of a View, we don't need to check whether each view is currently
   * visible or invisible.
   */
  void showErrorMessage() {
    mRecyclerView.setVisibility(View.INVISIBLE);
    mErrorMessageTextView.setVisibility(View.VISIBLE);
  }

  // TODO (6) Remove any and all code from MainActivity that references FetchWeatherTask
  private class FetchEmergencyTask extends AsyncTask<URL, Void, ArrayList<Incident>> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<Incident> doInBackground(URL... params) {
      if (params == null) {
        return null;
      }

      // get the URL from the parameters
      URL url = params[0];
      String results;
      //String[] formattedResults = null;

      try {
        // attempt to retrieve the JSON data from the server
        results = NetworkUtils.getResponseFromHttpUrl(url);
        incidents = FirelineJsonUtils.getIncidentsFromJson(results);
      } catch (IOException e) {
        e.printStackTrace();
      }

      return incidents;
    }

    @Override
    protected void onPostExecute(ArrayList<Incident> data) {
      // hiding progress bar
      mProgressBar.setVisibility(View.INVISIBLE);
      // if the data returned exists apply it within the TextView
      if (data != null) {
        showEmergencyData();
        // provide the new data to the IncidentAdapter to be bound to the ViewHolder
        mIncidentAdapter.setIncidentData(data);
      } else {
        showErrorMessage();
      }
    }
  }
}
