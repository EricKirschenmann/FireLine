package com.erickirschenmann.fireline;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/** Created by eric on 3/5/17. */
public class IncidentAdapter
    extends RecyclerView.Adapter<IncidentAdapter.IncidentAdapterViewHolder> {

  private String[] mIncidentData;

  public IncidentAdapter() {}

  @Override
  public IncidentAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.incident_list_item, parent, false);
    return new IncidentAdapterViewHolder(view);
  }

  @Override
  public void onBindViewHolder(IncidentAdapterViewHolder holder, int position) {
    String incidentData = mIncidentData[position];
    holder.mIncidentTextView.setText(incidentData);
  }

  @Override
  public int getItemCount() {
    if (mIncidentData == null) {
      return 0;
    }
    return mIncidentData.length;
  }

  public void setIncidentData(String[] incidentData) {
    mIncidentData = incidentData;
    notifyDataSetChanged();
  }

  class IncidentAdapterViewHolder extends RecyclerView.ViewHolder {
    public final TextView mIncidentTextView;

    public IncidentAdapterViewHolder(View itemView) {
      super(itemView);
      mIncidentTextView = (TextView) itemView.findViewById(R.id.tv_incident_data);
    }
  }
}
