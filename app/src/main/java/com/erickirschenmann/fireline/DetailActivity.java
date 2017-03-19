package com.erickirschenmann.fireline;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

  private TextView mDetailsTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    mDetailsTextView = (TextView) findViewById(R.id.tv_details);

    if (getActionBar() != null) {
      getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    Intent intent = getIntent();
    if (intent.hasExtra("INCIDENT_DETAILS")) {
      String message = intent.getStringExtra("INCIDENT_DETAILS");
      mDetailsTextView.setText(message);
    }

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
