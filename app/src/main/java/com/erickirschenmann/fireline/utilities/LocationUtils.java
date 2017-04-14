package com.erickirschenmann.fireline.utilities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.List;

/** Created by eric on 4/13/17. */
public class LocationUtils {
  public static LatLng getLocationFromAddress(Context context, String strAddress)
      throws IndexOutOfBoundsException {

    Geocoder coder = new Geocoder(context);
    List<Address> address;
    LatLng latLng;

    try {
      // May throw an IOException
      address = coder.getFromLocationName(strAddress, 5);
      if (address == null) {
        return null;
      }
      Address location = address.get(0);

      latLng = new LatLng(location.getLatitude(), location.getLongitude());

    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }

    return latLng;
  }
}
