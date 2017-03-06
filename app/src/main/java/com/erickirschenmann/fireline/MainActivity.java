package com.erickirschenmann.fireline;

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
import com.erickirschenmann.fireline.utilities.FirelineJsonUtils;
import com.erickirschenmann.fireline.utilities.NetworkUtils;
import java.io.IOException;
import java.net.URL;

// TODO (8) Implement ForecastAdapterOnClickHandler from the MainActivity
public class MainActivity extends AppCompatActivity {

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

    // TODO (11) Pass in 'this' as the ForecastAdapterOnClickHandler
    // responsible for linking the data to the ViewHolders
    mIncidentAdapter = new IncidentAdapter();

    mRecyclerView.setAdapter(mIncidentAdapter);

    mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

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

  // TODO (9) Override ForecastAdapterOnClickHandler's onClick method
  // TODO (10) Show a Toast when an item is clicked, displaying that item's weather data

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

  private class FetchEmergencyTask extends AsyncTask<URL, Void, String[]> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String[] doInBackground(URL... params) {
      if (params == null) {
        return null;
      }

      // get the URL from the parameters
      URL url = params[0];
      String results;
      String[] formattedResults = null;

      try {
        // attempt to retrieve the JSON data from the server
        results = NetworkUtils.getResponseFromHttpUrl(url);
        formattedResults = FirelineJsonUtils.getIncidentsFromJson(results);
      } catch (IOException e) {
        e.printStackTrace();
      }

      return formattedResults;
    }

    @Override
    protected void onPostExecute(String[] data) {
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
