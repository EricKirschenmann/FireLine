package com.erickirschenmann.fireline;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/** Created by eric on 3/5/17. */
// COMPLETED (15) Add a class file called ForecastAdapter
// COMPLETED (22) Extend RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>
public class IncidentAdapter
    extends RecyclerView.Adapter<IncidentAdapter.IncidentAdapterViewHolder> {
  // Within ForecastAdapter.java /////////////////////////////////////////////////////////////////

  // COMPLETED (23) Create a private string array called mWeatherData
  private String[] mIncidentData;

  // COMPLETED (47) Create the default constructor (we will pass in parameters in a later lesson)
  public IncidentAdapter() {}

  // COMPLETED (24) Override onCreateViewHolder
  @Override
  public IncidentAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // COMPLETED (25) Within onCreateViewHolder, inflate the list item xml into a view
    View view =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.incident_list_item, parent, false);
    // COMPLETED (26) Within onCreateViewHolder, return a new ForecastAdapterViewHolder with the above view passed in as a parameter
    return new IncidentAdapterViewHolder(view);
  }

  // COMPLETED (27) Override onBindViewHolder
  // COMPLETED (28) Set the text of the TextView to the weather for this list item's position
  @Override
  public void onBindViewHolder(IncidentAdapterViewHolder holder, int position) {
    String incidentData = mIncidentData[position];
    holder.mIncidentTextView.setText(incidentData);
  }

  // COMPLETED (29) Override getItemCount
  @Override
  public int getItemCount() {
    // COMPLETED (30) Return 0 if mWeatherData is null, or the size of mWeatherData if it is not null
    if (mIncidentData == null) {
      return 0;
    }
    return mIncidentData.length;
  }

  // COMPLETED (31) Create a setWeatherData method that saves the weatherData to mWeatherData
  public void setIncidentData(String[] incidentData) {
    mIncidentData = incidentData;
    // COMPLETED (32) After you save mWeatherData, call notifyDataSetChanged
    notifyDataSetChanged();
  }

  // COMPLETED (16) Create a class within ForecastAdapter called ForecastAdapterViewHolder
  // COMPLETED (17) Extend RecyclerView.ViewHolder
  class IncidentAdapterViewHolder extends RecyclerView.ViewHolder {
    // Within ForecastAdapterViewHolder ///////////////////////////////////////////////////////////
    // COMPLETED (18) Create a public final TextView variable called mWeatherTextView
    public final TextView mIncidentTextView;

    // COMPLETED (19) Create a constructor for this class that accepts a View as a parameter
    public IncidentAdapterViewHolder(View itemView) {
      // COMPLETED (20) Call super(view) within the constructor for ForecastAdapterViewHolder
      super(itemView);
      // COMPLETED (21) Using view.findViewById, get a reference to this layout's TextView and save it to mWeatherTextView
      mIncidentTextView = (TextView) itemView.findViewById(R.id.tv_incident_data);
    }

    // Within ForecastAdapterViewHolder ///////////////////////////////////////////////////////////
  }
  // Within ForecastAdapter.java /////////////////////////////////////////////////////////////////
}
