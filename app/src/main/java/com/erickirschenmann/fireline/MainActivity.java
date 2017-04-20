package com.erickirschenmann.fireline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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

public class MainActivity extends AppCompatActivity
    implements IncidentAdapterOnClickHandler,
        LoaderCallbacks<ArrayList<Incident>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

  private static final int INCIDENT_LOADER_ID = 19232;
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

    // initial load of data
    getSupportLoaderManager().initLoader(INCIDENT_LOADER_ID, null, this);

    // register the OnSharedPreferenceChangeListener so the data will update when the user changes a preference
    PreferenceManager.getDefaultSharedPreferences(this)
        .registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    // reload the data
    reloadData();
  }

  @Override
  protected void onDestroy() {
    PreferenceManager.getDefaultSharedPreferences(this)
        .unregisterOnSharedPreferenceChangeListener(this);
    super.onDestroy();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  private void reloadData() {
    invalidateData();
    getSupportLoaderManager().restartLoader(INCIDENT_LOADER_ID, null, this);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    // check which item was selected
    switch (item.getItemId()) {
      case R.id.action_refresh:
        // when the refresh selected refresh the data
        reloadData();
        return true;
      case R.id.action_settings:
        // open settings activity
        startActivity(new Intent(this, SettingsActivity.class));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void invalidateData() {
    mIncidentAdapter.setIncidentData(null);
  }

  /**
   * Instantiate and return a new Loader for the given ID.
   *
   * @param id The ID whose loader is to be created.
   * @param args Any arguments supplied by the caller.
   * @return Return a new Loader instance that is ready to start loading.
   */
  @Override
  public Loader<ArrayList<Incident>> onCreateLoader(int id, Bundle args) {
    return new AsyncTaskLoader<ArrayList<Incident>>(this) {

      ArrayList<Incident> mIncidents;

      @Override
      protected void onStartLoading() {
        if (mIncidents != null) {
          deliverResult(mIncidents);
        } else {
          mProgressBar.setVisibility(View.VISIBLE);
          forceLoad();
        }
      }

      @Override
      public ArrayList<Incident> loadInBackground() {
        String results;

        try {
          // attempt to retrieve the JSON data from the server
          URL url = NetworkUtils.getUrl();
          results = NetworkUtils.getResponseFromHttpUrl(url, getContext());
          incidents = FirelineJsonUtils.getIncidentsFromJson(getContext(), results);
        } catch (IOException e) {
          e.printStackTrace();
        }

        return incidents;
      }

      @Override
      public void deliverResult(ArrayList<Incident> data) {
        mIncidents = data;
        super.deliverResult(data);
      }
    };
  }

  @Override
  public void onLoadFinished(Loader<ArrayList<Incident>> loader, ArrayList<Incident> data) {
    mProgressBar.setVisibility(View.INVISIBLE);
    mIncidentAdapter.setIncidentData(data);
    if (data == null) {
      showErrorMessage();
    } else {
      showEmergencyData();
    }
  }

  @Override
  public void onLoaderReset(Loader<ArrayList<Incident>> loader) {}

  /**
   * Handles the click on one of the RecyclerView items
   *
   * @param incident The Parcelable Incident object representing the selected Incident
   */
  @Override
  public void onClick(Incident incident) {
    Intent detailsIntent = new Intent(this, DetailActivity.class);
    detailsIntent.putExtra("INCIDENT_EXTRA", incident);
    startActivity(detailsIntent);
  }

  /**
   * This method will make the View for the JSON data visible and hide the error message. Since it
   * is okay to redundantly set the visibility of a View, we don't need to check whether each view
   * is currently visible or invisible.
   */
  private void showEmergencyData() {
    mRecyclerView.setVisibility(View.VISIBLE);
    mErrorMessageTextView.setVisibility(View.INVISIBLE);
  }

  /**
   * This method will make the error message visible and hide the JSON View. Since it is okay to
   * redundantly set the visibility of a View, we don't need to check whether each view is currently
   * visible or invisible.
   */
  private void showErrorMessage() {
    mRecyclerView.setVisibility(View.INVISIBLE);
    mErrorMessageTextView.setVisibility(View.VISIBLE);
  }
}
