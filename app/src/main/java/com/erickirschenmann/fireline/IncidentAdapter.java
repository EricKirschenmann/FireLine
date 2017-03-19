package com.erickirschenmann.fireline;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.erickirschenmann.fireline.models.Incident;
import java.util.ArrayList;

/**
 * {@link IncidentAdapter} exposes a list of incident details to a {@link
 * android.support.v7.widget.RecyclerView}
 */
class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.IncidentAdapterViewHolder> {

  private final IncidentAdapterOnClickHandler mClickHandler;
  private String[] mIncidentData;
  private String[] mIncidentType;
  private ArrayList<Incident> mIncidents;

  IncidentAdapter(IncidentAdapterOnClickHandler onClickHandler) {
    mClickHandler = onClickHandler;
  }

  /**
   * This gets called when each new ViewHolder is created. This happens when the RecyclerView is
   * laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
   *
   * @param parent The ViewGroup that these ViewHolders are contained within.
   * @param viewType If your RecyclerView has more than one type of item (which ours doesn't) you
   *     can use this viewType integer to provide a different layout. See {@link
   *     android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)} for more details.
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
   *     the given position in the data set.
   * @param position The position of the item within the adapter's data set.
   */
  @Override
  public void onBindViewHolder(IncidentAdapterViewHolder holder, int position) {
    String incidentData = mIncidentData[position];
    String incidentType = mIncidentType[position];

    holder.mIncidentTextView.setText(incidentData);
    holder.mIncidentTypeTextView.setText(incidentType);
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
   * @param incidents The new ArrayList of Incidents.
   */
  void setIncidentData(ArrayList<Incident> incidents) {
    mIncidents = incidents;
    String[] formattedIncidents;
    String[] incidentTypes;

    if (mIncidents != null && mIncidents.size() != 0) {
      // using the incident objects get the toString()
      formattedIncidents = new String[mIncidents.size()];
      incidentTypes = new String[mIncidents.size()];
      for (int x = 0; x < formattedIncidents.length; x++) {
        formattedIncidents[x] = mIncidents.get(x).toString();
        incidentTypes[x] = mIncidents.get(x).getIncidentType();
      }
    } else {
      formattedIncidents = null;
      incidentTypes = null;
    }

    // now store the updated data
    mIncidentData = formattedIncidents;
    mIncidentType = incidentTypes;
    notifyDataSetChanged();
  }

  interface IncidentAdapterOnClickHandler {

    void onClick(String incidentData);
  }

  /** Cache of the children views for an incident list item. */
  class IncidentAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

    final TextView mIncidentTextView;
    final TextView mIncidentTypeTextView;

    IncidentAdapterViewHolder(View itemView) {
      super(itemView);
      mIncidentTextView = (TextView) itemView.findViewById(R.id.tv_incident_data);
      mIncidentTypeTextView = (TextView) itemView.findViewById(R.id.tv_incident_type);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      int position = getAdapterPosition();
      String details = mIncidents.get(position).toString();

      // test that the correct incident data is being grabbed
      // String latitude = Double.toString(mIncidents.get(position).getLatitude());
      // String longitude = Double.toString(mIncidents.get(position).getLongitude());
      // String address = mIncidents.get(position).getStreetAddress();

      // this will be converted into a google maps intent
      // String gmmIntent = "geo:" + latitude + "," + longitude + "?q=" + address;

      mClickHandler.onClick(details);
    }
  }
}
