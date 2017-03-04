package com.erickirschenmann.fireline;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.erickirschenmann.fireline.utilities.NetworkUtils;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

  private TextView mEmergencyTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mEmergencyTextView = (TextView) findViewById(R.id.tv_fireline_results);
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
        // when the refresh selected display a placeholder Toast message
        Toast.makeText(this, getString(R.string.action_refresh), Toast.LENGTH_SHORT).show();
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

  class FetchEmergencyTask extends AsyncTask<URL, Void, String> {

    @Override
    protected String doInBackground(URL... params) {
      if (params == null) {
        return null;
      }

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
      if (s != null && !s.equals("")) {
        mEmergencyTextView.setText(s);
      }
    }
  }
}
