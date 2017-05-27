package com.erickirschenmann.fireline;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.erickirschenmann.fireline.IncidentAdapter.IncidentAdapterOnClickHandler;
import com.erickirschenmann.fireline.models.Incident;
import com.erickirschenmann.fireline.utilities.FirelineJsonUtils;
import com.erickirschenmann.fireline.utilities.NetworkUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity
    implements IncidentAdapterOnClickHandler,
        LoaderCallbacks<ArrayList<Incident>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

  private static final String TAG = MainActivity.class.getSimpleName();
  private static final int INCIDENT_LOADER_ID = 19232;
  private ArrayList<Incident> incidents;
  private RecyclerView mRecyclerView;
  private IncidentAdapter mIncidentAdapter;
  private TextView mErrorMessageTextView;
  private ProgressBar mProgressBar;
  private Toast mToast;

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

    resetDebug();
    checkFail();

    // initial load of data
    getSupportLoaderManager().initLoader(INCIDENT_LOADER_ID, null, this);

    // register the OnSharedPreferenceChangeListener so the data will update when the user changes a preference
    PreferenceManager.getDefaultSharedPreferences(this)
        .registerOnSharedPreferenceChangeListener(this);
  }

  void resetDebug() {
    boolean debug = getResources().getBoolean(R.bool.debug);
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    // if the app isn't in debug and the data might be debug data reset it
    if (!debug && sharedPreferences.contains(getString(R.string.pref_show_debug_key))) {
      SharedPreferences.Editor editor = sharedPreferences.edit();
      editor.putBoolean(getString(R.string.pref_show_debug_key), false);
      editor.apply();
    }
  }

  void checkFail() {
    final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    if (sharedPreferences.contains(getString(R.string.pref_location_failed_key))) {
      System.out.println("here");
      if (sharedPreferences.getBoolean(getString(R.string.pref_location_failed_key), false)) {
        AlertDialog.Builder builder = new Builder(this);
        builder.setMessage(R.string.pref_location_failed_message);
        builder.setPositiveButton(
            R.string.pref_location_failed_okay,
            new OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // undo the error message so it doesn't show up all the time
                editor.putBoolean(getString(R.string.pref_location_failed_key), false);
                editor.apply();
              }
            });
        builder.show();
      }
    }
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

  /** Invalidate the current data, then begin new Loader to retrieve new data */
  private void reloadData() {
    resetDebug(); // just to make sure...
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
      case R.id.action_sort:
        // sort the data
        sortData();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void invalidateData() {
    mIncidentAdapter.setIncidentData(null);
  }

  /**
   * Attempt to sort the {@code ArrayList} of Incident objects based on the distance from the user's
   * specified location and display a message if the sort is successful or unsuccessful
   */
  private void sortData() {
    // store the different messages depending on the sort
    String message;

    try {
      // for some reason the sort crashes on a length above 150 or so so just trim the size of the list,
      // most likely a bad value in one of the first 15 or so values, but it's just debug data so oh well
      // hopefully none of the fire department's values crash it
      int maxSize = 50;

      // make sure the ArrayList is not too long to be sorted by Collections
      if (incidents.size() <= maxSize) {
        Collections.sort(incidents);
        mIncidentAdapter.setIncidentData(incidents);
        message = getString(R.string.sort_distance_message);
      } else {
        message = getString(R.string.sort_error_message);
      }
    } catch (IllegalArgumentException e) {
      Log.e(TAG, "sortData: could not sort the data!");
      message = getString(R.string.sort_error_message);
    }

    // check if the sort has already happened and reset the toast
    if (mToast != null) {
      mToast.cancel();
    }
    // display the toast with the message
    mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
    mToast.show();
  }

  /**
   * Instantiate and return a new Loader for the given ID.
   *
   * @param id The ID whose loader is to be created.
   * @param args Any arguments supplied by the caller.
   * @return Return a new Loader instance that is ready to start loading.
   */
  @SuppressLint("StaticFieldLeak")
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
