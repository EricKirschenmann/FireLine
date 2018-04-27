package com.erickirschenmann.fireline.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.maps.model.LatLng;
import java.util.Arrays;

/** Created by eric on 3/4/17. */
public class Incident implements Parcelable {

  public static final Creator<Incident> CREATOR =
      new Creator<Incident>() {
        @Override
        public Incident createFromParcel(Parcel source) {
          return new Incident(source);
        }

        @Override
        public Incident[] newArray(int size) {
          return new Incident[size];
        }
      };
  // instance variables that will contain all the details for a specific incident
  private String address; // the street not full address
  private String block; // closest block, will not be an exact location
  private String city;
  private String comment;
  private String incidentNumber;
  private String incidentType; // possibly change to enum
  private double latitude;
  private double longitude;
  private String responseDate;
  private String status;
  private String[] units;
  private LatLng latLng;
  private double distance;

  // should never be used
  public Incident() {
    this.address = "";
    this.block = "";
    this.city = "";
    this.comment = "";
    this.incidentNumber = "";
    this.incidentType = "";
    this.latitude = 0.0;
    this.longitude = 0.0;
    this.responseDate = "";
    this.status = "";
    this.units = new String[1];
    this.latLng = new LatLng(this.latitude, this.longitude);
    this.distance = 0.0;
  }

  /**
   * Overloaded constructor will be used to create Incident objects containing all the details of
   * the current incident
   *
   * @param address The street location of the incident
   * @param block The closest city block to the incident
   * @param city The city that the location is in
   * @param comment The comment provided
   * @param incidentNumber The, hopefully, unique incident number
   * @param incidentType The type of incident, i.e. traffic collision or medical
   * @param latitude The latitude of the incident
   * @param longitude The longitude of the incident
   * @param responseDate The day the incident was received and responded to
   * @param status The current status, usually "en route" or "on scene"
   * @param units The different units assigned to the incident
   */
  public Incident(
      String address,
      String block,
      String city,
      String comment,
      String incidentNumber,
      String incidentType,
      double latitude,
      double longitude,
      String responseDate,
      String status,
      String units,
      LatLng latLng,
      double distance) {
    setAddress(address.trim(), block.trim());
    this.block = block.trim();
    this.city = city.trim();
    this.comment = comment.trim();
    this.incidentNumber = incidentNumber.trim();
    // this.incidentType = incidentType;
    setType(incidentType);
    this.latitude = latitude;
    this.longitude = longitude;
    this.responseDate = responseDate;
    this.status = status;
    getUnitsArray(units); // hopefully will get the units
    this.latLng = latLng;
    this.distance = distance;
  }

  private Incident(Parcel in) {
    this.address = in.readString();
    this.block = in.readString();
    this.city = in.readString();
    this.comment = in.readString();
    this.incidentNumber = in.readString();
    this.incidentType = in.readString();
    this.latitude = in.readDouble();
    this.longitude = in.readDouble();
    this.responseDate = in.readString();
    this.status = in.readString();
    this.units = in.createStringArray();
    this.latLng = in.readParcelable(LatLng.class.getClassLoader());
    this.distance = in.readDouble();
  }

  public double getDistance() {
    return distance;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }

  /**
   * Sets the incident type to a more readable version for those that are abbreviated currently only
   * TC -> Traffic Collision more added later if they exist
   *
   * @param incidentType The {@code String} containing the provided incident type
   */
  private void setType(String incidentType) {
    if (incidentType.contains("TC")) {
      this.setIncidentType(incidentType.replace("TC", "Traffic Collision"));
    } else {
      this.setIncidentType(incidentType);
    }
  }

  /**
   * This is better and haven't run into issues, yet...
   */
  private void getUnitsArray(String unitString) {
    this.units = new String[unitString.split("\\s*,\\s*").length];
    this.units = unitString.trim().split("\\s*,\\s*");
  }

  // getters and setters which will most likely never be used but just in case
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  private void setAddress(String address, String block) {
    // remove the block info from the address since it's in it's own variable
    if (address.contains(block)) {
      address = address.replace(block, "");
    }

    // remove weird characters
    if (address.contains("-")) {
      address = address.replace("-", " ");
    }

    this.address = address.trim();
  }

  public String getBlock() {
    return block;
  }

  public void setBlock(String block) {
    this.block = block;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  private String getComment() {
    if (comment.equals("")) {
      return "No comments.";
    } else {
      return comment;
    }
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getIncidentNumber() {
    return incidentNumber;
  }

  public void setIncidentNumber(String incidentNumber) {
    this.incidentNumber = incidentNumber;
  }

  public String getIncidentType() {
    return incidentType;
  }

  private void setIncidentType(String incidentType) {
    this.incidentType = toTitleCase(incidentType);
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getResponseDate() {
    return responseDate;
  }

  public void setResponseDate(String responseDate) {
    this.responseDate = responseDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String[] getUnits() {
    return units;
  }

  public void setUnits(String[] units) {
    this.units = units;
  }

  public LatLng getLatLng() {
    return latLng;
  }

  public void setLatLng(LatLng latLng) {
    this.latLng = latLng;
  }

  /**
   * Get the units array as a comma separated list of units
   *
   * @return A {@code String} representation of the units array
   */
  private String getUnitsString() {
    // hopefully fix crashing issue by not returning some sort of null pointer
    StringBuilder stringBuilder = new StringBuilder();
    if (this.units != null && this.units.length != 0) {
      for (int x = 0; x < this.units.length; x++) {
        if (x != this.units.length - 1) {
          stringBuilder.append(this.units[x]).append(", ");
        } else {
          stringBuilder.append(this.units[x]);
        }
      }
    }

    return stringBuilder.toString();
  }

  /**
   * Get the block and the street name as a single String
   *
   * @return The full street address of this Incident
   */
  public String getStreetAddress() {
    if (this.block.equals("")) {
      return this.address;
    } else {
      return this.block + " " + this.address;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Incident incident = (Incident) o;

    if (Double.compare(incident.latitude, latitude) != 0) {
      return false;
    }
    if (Double.compare(incident.longitude, longitude) != 0) {
      return false;
    }
    if (address != null ? !address.equals(incident.address) : incident.address != null) {
      return false;
    }
    if (block != null ? !block.equals(incident.block) : incident.block != null) {
      return false;
    }
    if (city != null ? !city.equals(incident.city) : incident.city != null) {
      return false;
    }
    if (comment != null ? !comment.equals(incident.comment) : incident.comment != null) {
      return false;
    }
    if (incidentNumber != null
        ? !incidentNumber.equals(incident.incidentNumber)
        : incident.incidentNumber != null) {
      return false;
    }
    if (incidentType != null
        ? !incidentType.equals(incident.incidentType)
        : incident.incidentType != null) {
      return false;
    }
    if (responseDate != null
        ? !responseDate.equals(incident.responseDate)
        : incident.responseDate != null) {
      return false;
    }
    if (status != null ? !status.equals(incident.status) : incident.status != null) {
      return false;
    }
    // Probably incorrect - comparing Object[] arrays with Arrays.equals
    return Arrays.equals(units, incident.units);
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = address != null ? address.hashCode() : 0;
    result = 31 * result + (block != null ? block.hashCode() : 0);
    result = 31 * result + (city != null ? city.hashCode() : 0);
    result = 31 * result + (comment != null ? comment.hashCode() : 0);
    result = 31 * result + (incidentNumber != null ? incidentNumber.hashCode() : 0);
    result = 31 * result + (incidentType != null ? incidentType.hashCode() : 0);
    temp = Double.doubleToLongBits(latitude);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(longitude);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (responseDate != null ? responseDate.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + Arrays.hashCode(units);
    return result;
  }

  @Override
  public String toString() {
    if (!this.city.isEmpty()) {
      return toTitleCase(this.responseDate + "\n" + this.getStreetAddress() + ", " + this.city);
    } else {
      return toTitleCase(this.responseDate + "\n" + this.getStreetAddress());
    }
  }

  private String toTitleCase(String s) {
    final String ACTIONABLE_DELIMITERS = " '-/\n"; // these cause the character following
    // to be capitalized, need new line since the string coming into this is split by new lines

    StringBuilder sb = new StringBuilder();
    boolean capNext = true;

    for (char c : s.toCharArray()) {
      c = (capNext)
          ? Character.toUpperCase(c)
          : Character.toLowerCase(c);
      sb.append(c);
      capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
    }
    return sb.toString();
  }

  public String getDetails() {
    return this.responseDate
        + "\nIncident Number: "
        + this.incidentNumber
        + "\nIncident Type: "
        + this.incidentType
        + "\nAddress: "
        + this.getStreetAddress()
        + "\nCity: "
        + this.city
        + "\nUnits: "
        + this.getUnitsString()
        + "\nStatus: "
        + this.status
        + "\nComments: "
        + this.getComment();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.address);
    dest.writeString(this.block);
    dest.writeString(this.city);
    dest.writeString(this.comment);
    dest.writeString(this.incidentNumber);
    dest.writeString(this.incidentType);
    dest.writeDouble(this.latitude);
    dest.writeDouble(this.longitude);
    dest.writeString(this.responseDate);
    dest.writeString(this.status);
    dest.writeStringArray(this.units);
    dest.writeParcelable(this.latLng, flags);
    dest.writeDouble(this.distance);
  }
}
