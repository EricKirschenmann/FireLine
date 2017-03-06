package com.erickirschenmann.fireline;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * {@link IncidentAdapter} exposes a list of incident details to a {@link
 * android.support.v7.widget.RecyclerView}
 */
class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.IncidentAdapterViewHolder> {

  private String[] mIncidentData;

  // TODO (3) Create a final private ForecastAdapterOnClickHandler called mClickHandler

  // TODO (1) Add an interface called ForecastAdapterOnClickHandler
  // TODO (2) Within that interface, define a void method that access a String as a parameter

  // TODO (4) Add a ForecastAdapterOnClickHandler as a parameter to the constructor and store it in mClickHandler


  IncidentAdapter() {
  }

  // TODO (5) Implement OnClickListener in the ForecastAdapterViewHolder class

  /**
   * This gets called when each new ViewHolder is created. This happens when the RecyclerView is
   * laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
   *
   * @param parent The ViewGroup that these ViewHolders are contained within.
   * @param viewType If your RecyclerView has more than one type of item (which ours doesn't) you
   * can use this viewType integer to provide a different layout. See {@link
   * android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)} for more details.
   * @return A new IncidentAdapterViewHolder that holds the View for each list item
   */
  @Override
  public IncidentAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.incident_list_item, parent, false);
    return new IncidentAdapterViewHolder(view);
  }

  /**
   * OnBindViewHolder is called by the RecyclerView to display the data at the specified position.
   * In this method, we update the contents of the ViewHolder to display the incident details for
   * this particular position, using the "position" argument that is conveniently passed into us.
   *
   * @param holder The ViewHolder which should be updated to represent the contents of the item at
   * the given position in the data set.
   * @param position The position of the item within the adapter's data set.
   */
  @Override
  public void onBindViewHolder(IncidentAdapterViewHolder holder, int position) {
    String incidentData = mIncidentData[position];
    holder.mIncidentTextView.setText(incidentData);
  }

  /**
   * This method simply returns the number of items to display. It is used behind the scenes to help
   * layout our Views and for animations.
   *
   * @return The number of items available in our incident list
   */
  @Override
  public int getItemCount() {
    if (mIncidentData == null) {
      return 0;
    }
    return mIncidentData.length;
  }

  /**
   * This method is used to set the incident details on a IncidentAdapter if we've already created
   * one. This is handy when we get new data from the web but don't want to create a new
   * IncidentAdapter to display it.
   *
   * @param incidentData The new incident data to be displayed.
   */
  void setIncidentData(String[] incidentData) {
    mIncidentData = incidentData;
    notifyDataSetChanged();
  }

  /**
   * Cache of the children views for an incident list item.
   */
  class IncidentAdapterViewHolder extends RecyclerView.ViewHolder {

    final TextView mIncidentTextView;

    IncidentAdapterViewHolder(View itemView) {
      super(itemView);
      mIncidentTextView = (TextView) itemView.findViewById(R.id.tv_incident_data);
      // TODO (7) Call setOnClickListener on the view passed into the constructor (use 'this' as the OnClickListener)

    }

    // TODO (6) Override onClick, passing the clicked day's data to mClickHandler via its onClick method

  }
}
