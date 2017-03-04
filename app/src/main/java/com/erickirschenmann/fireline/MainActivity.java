package com.erickirschenmann.fireline;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.erickirschenmann.fireline.utilities.NetworkUtils;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

  private TextView mEmergencyTextView;
  private TextView mErrorMessageTextView;
  private ProgressBar mProgressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mEmergencyTextView = (TextView) findViewById(R.id.tv_fireline_results);
    mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
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

  /**
   * This method will make the View for the JSON data visible and hide the error message. Since it
   * is okay to redundantly set the visibility of a View, we don't need to check whether each view
   * is currently visible or invisible.
   */
  void showEmergencyData() {
    mEmergencyTextView.setVisibility(View.VISIBLE);
    mErrorMessageTextView.setVisibility(View.INVISIBLE);
  }

  /**
   * This method will make the error message visible and hide the JSON View. Since it is okay to
   * redundantly set the visibility of a View, we don't need to check whether each view is currently
   * visible or invisible.
   */
  void showErrorMessage() {
    mEmergencyTextView.setVisibility(View.INVISIBLE);
    mErrorMessageTextView.setVisibility(View.VISIBLE);
  }

  private class FetchEmergencyTask extends AsyncTask<URL, Void, String> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(URL... params) {
      if (params == null) {
        return null;
      }

      // get the URL from the parameters
      URL url = params[0];
      String results = null;

      try {
        // attempt to retrieve the JSON data from the server
        results = NetworkUtils.getResponseFromHttpUrl(url);
      } catch (IOException e) {
        e.printStackTrace();
      }

      return results;
    }

    @Override
    protected void onPostExecute(String s) {
      // hiding progress bar
      mProgressBar.setVisibility(View.INVISIBLE);
      // if the data returned exists apply it within the TextView
      if (s != null && !s.equals("")) {
        showEmergencyData();
        mEmergencyTextView.setText(s);
      } else {
        showErrorMessage();
      }
    }
  }
}
