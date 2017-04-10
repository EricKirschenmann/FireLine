package com.erickirschenmann.fireline;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.erickirschenmann.fireline.models.Incident;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

  private static final String LOG_TAG = DetailActivity.class.getSimpleName();
  private TextView mDetailsTextView;
  private double mLat = 0;
  private double mLong = 0;
  private String mAddress = "";

  private Incident mIncident;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    mDetailsTextView = (TextView) findViewById(R.id.tv_details);

    if (getActionBar() != null) {
      getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    Intent intent = getIntent();

    if (intent.hasExtra("INCIDENT_EXTRA")) {
      mIncident = intent.getParcelableExtra("INCIDENT_EXTRA");
    }

    if (mIncident != null) {
      mAddress = mIncident.getStreetAddress() + ", " + mIncident.getCity();
      mLat = mIncident.getLatitude();
      mLong = mIncident.getLongitude();
      setDetails();
    }

    // Get the SupportMapFragment and request notification
    // when the map is ready to be used.
    SupportMapFragment mapFragment =
        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
    mapFragment.getMapAsync(this);
  }

  /** Sets the details TextView, will be filled in separately at a later point */
  void setDetails() {
    mDetailsTextView.setText(mIncident.getDetails());
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        return true;
      case R.id.action_settings:
        startActivity(new Intent(this, SettingsActivity.class));
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.details, menu);
    return true;
  }

  /**
   * Manipulates the map when it's available. The API invokes this callback when the map is ready to
   * be used. This is where we can add markers or lines, add listeners or move the camera. In this
   * case, we just add a marker near Sydney, Australia. If Google Play services is not installed on
   * the device, the user receives a prompt to install Play services inside the SupportMapFragment.
   * The API invokes this method after the user has installed Google Play services and returned to
   * the app.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    // Add a marker in Sydney, Australia,
    // and move the map's camera to the same location.

    //LatLng location = getLocationFromAddress(this, mAddress);
    LatLng location;

    // intersections don't work so use that latitude and longitude
    if (mAddress.contains("/")) {
      location = new LatLng(mLat, mLong);
    } else {
      try {
        // this is the ideal scenario as it is a more accurate location than the provided lat and long
        location = getLocationFromAddress(this, mAddress);
      } catch (IndexOutOfBoundsException e) {
        // for some reason it does not like certain address so this should hopefully just happen by default
        location = new LatLng(mLat, mLong);
      }
    }

    // place the marker on the map
    googleMap.addMarker(new MarkerOptions().position(location).title(mAddress));

    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    googleMap.animateCamera(CameraUpdateFactory.zoomIn());
    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    googleMap.setTrafficEnabled(true);
  }

  public LatLng getLocationFromAddress(Context context, String strAddress)
      throws IndexOutOfBoundsException {

    Geocoder coder = new Geocoder(context);
    List<Address> address;
    LatLng p1;

    try {
      // May throw an IOException
      address = coder.getFromLocationName(strAddress, 5);
      if (address == null) {
        return null;
      }
      Address location = address.get(0);

      p1 = new LatLng(location.getLatitude(), location.getLongitude());

    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }

    return p1;
  }
}
