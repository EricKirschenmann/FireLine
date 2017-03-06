package com.erickirschenmann.fireline;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.erickirschenmann.fireline.utilities.FirelineJsonUtils;
import com.erickirschenmann.fireline.utilities.NetworkUtils;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

  // Within forecast_list_item.xml //////////////////////////////////////////////////////////////
  // TODO (5) Add a layout for an item in the list called forecast_list_item.xml
  // TODO (6) Make the root of the layout a vertical LinearLayout
  // TODO (7) Set the width of the LinearLayout to match_parent and the height to wrap_content

  // TODO (8) Add a TextView with an id @+id/tv_weather_data
  // TODO (9) Set the text size to 22sp
  // TODO (10) Make the width and height wrap_content
  // TODO (11) Give the TextView 16dp of padding

  // TODO (12) Add a View to the layout with a width of match_parent and a height of 1dp
  // TODO (13) Set the background color to #dadada
  // TODO (14) Set the left and right margins to 8dp
  // Within forecast_list_item.xml //////////////////////////////////////////////////////////////

  // Within ForecastAdapter.java /////////////////////////////////////////////////////////////////
  // TODO (15) Add a class file called ForecastAdapter
  // TODO (22) Extend RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>

  // TODO (23) Create a private string array called mWeatherData

  // TODO (47) Create the default constructor (we will pass in parameters in a later lesson)

  // TODO (16) Create a class within ForecastAdapter called ForecastAdapterViewHolder
  // TODO (17) Extend RecyclerView.ViewHolder

  // Within ForecastAdapterViewHolder ///////////////////////////////////////////////////////////
  // TODO (18) Create a public final TextView variable called mWeatherTextView

  // TODO (19) Create a constructor for this class that accepts a View as a parameter
  // TODO (20) Call super(view) within the constructor for ForecastAdapterViewHolder
  // TODO (21) Using view.findViewById, get a reference to this layout's TextView and save it to mWeatherTextView
  // Within ForecastAdapterViewHolder ///////////////////////////////////////////////////////////

  // TODO (24) Override onCreateViewHolder
  // TODO (25) Within onCreateViewHolder, inflate the list item xml into a view
  // TODO (26) Within onCreateViewHolder, return a new ForecastAdapterViewHolder with the above view passed in as a parameter

  // TODO (27) Override onBindViewHolder
  // TODO (28) Set the text of the TextView to the weather for this list item's position

  // TODO (29) Override getItemCount
  // TODO (30) Return 0 if mWeatherData is null, or the size of mWeatherData if it is not null

  // TODO (31) Create a setWeatherData method that saves the weatherData to mWeatherData
  // TODO (32) After you save mWeatherData, call notifyDataSetChanged
  // Within ForecastAdapter.java /////////////////////////////////////////////////////////////////

  // TODO (33) Delete mEmergencyTextView
  private TextView mEmergencyTextView;

  // TODO (34) Add a private RecyclerView variable called mRecyclerView
  // TODO (35) Add a private ForecastAdapter variable called mForecastAdapter

  private TextView mErrorMessageTextView;
  private ProgressBar mProgressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // TODO (36) Delete the line where you get a reference to mWeatherTextView
    mEmergencyTextView = (TextView) findViewById(R.id.tv_fireline_results);

    // TODO (37) Use findViewById to get a reference to the RecyclerView

    mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);

    // TODO (38) Create layoutManager, a LinearLayoutManager with VERTICAL orientation and shouldReverseLayout == false

    // TODO (39) Set the layoutManager on mRecyclerView

    // TODO (40) Use setHasFixedSize(true) on mRecyclerView to designate that all items in the list will have the same size

    // TODO (41) set mForecastAdapter equal to a new ForecastAdapter

    // TODO (42) Use mRecyclerView.setAdapter and pass in mForecastAdapter

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
        // TODO (46) Instead of setting the text to "", set the adapter to null before refreshing
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
    // TODO (43) Show mRecyclerView, not mWeatherTextView
    mEmergencyTextView.setVisibility(View.VISIBLE);
    mErrorMessageTextView.setVisibility(View.INVISIBLE);
  }

  /**
   * This method will make the error message visible and hide the JSON View. Since it is okay to
   * redundantly set the visibility of a View, we don't need to check whether each view is currently
   * visible or invisible.
   */
  void showErrorMessage() {
    // TODO (44) Hide mRecyclerView, not mWeatherTextView
    mEmergencyTextView.setVisibility(View.INVISIBLE);
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
        mEmergencyTextView.setText("");
        // TODO (45) Instead of iterating through every string, use mForecastAdapter.setWeatherData and pass in the weather data
        // create temporary list within TextView, will be replaced with RecyclerView eventually
        for (String s : data) {
          mEmergencyTextView.append(s + "\n\n\n");
        }

      } else {
        showErrorMessage();
      }
    }
  }
}
