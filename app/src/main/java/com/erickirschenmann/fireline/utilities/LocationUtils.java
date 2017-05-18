package com.erickirschenmann.fireline.utilities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.List;

/** Created by eric on 4/13/17. */
public class LocationUtils {

  /**
   * Converts a street address into a {@code LatLng} object which is more usable by Google Maps
   *
   * @param context the context calling the method
   * @param address a {@code String} containing the street address to be converted
   * @return a {@code LatLng} object representation of the provided street address
   * @throws IndexOutOfBoundsException if no valid LatLng created
   */
  public static LatLng getLocationFromAddress(Context context, String address)
      throws IndexOutOfBoundsException {

    Geocoder geocoder = new Geocoder(context);
    List<Address> addressList;
    LatLng latLng;

    try {
      // May throw an IOException
      addressList = geocoder.getFromLocationName(address, 5);
      if (addressList == null) {
        return null;
      }
      Address location = addressList.get(0);

      latLng = new LatLng(location.getLatitude(), location.getLongitude());

    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }

    return latLng;
  }

  static LatLng getLocationFromAddress(
      Context context, String address, double latitude, double longitude)
      throws IndexOutOfBoundsException {

    Geocoder geocoder = new Geocoder(context);
    List<Address> addressList;
    LatLng latLng;

    try {
      if (address.contains("/")) {
        latLng = new LatLng(latitude, longitude);
        return latLng;
      }

      // May throw an IOException
      addressList = geocoder.getFromLocationName(address, 5);
      if (addressList == null) {
        return null;
      }
      Address location = addressList.get(0);

      latLng = new LatLng(location.getLatitude(), location.getLongitude());

    } catch (IndexOutOfBoundsException e) {
      latLng = new LatLng(latitude, longitude);
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }

    return latLng;
  }
}
