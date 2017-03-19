package com.erickirschenmann.fireline.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

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

  // should never be used
  public Incident() {
    this.address = "Hill St / S C St";
    this.block = "";
    this.city = "Oxnard";
    this.comment = "";
    this.incidentNumber = "";
    this.incidentType = "";
    this.latitude = 0.0;
    this.longitude = 0.0;
    this.responseDate = "";
    this.status = "";
    this.units = new String[1];
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
      String units) {
    this.address = address.trim();
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
  }

  /**
   * Sets the incident type to a more readable version for those that are abbreviated currently only
   * TC -> Traffic Collision more added later if they exist
   *
   * @param incidentType The {@code String} containing the provided incident type
   */
  private void setType(String incidentType) {
    if (incidentType.equals("TC")) {
      this.setIncidentType("Traffic Collision");
    } else {
      this.setIncidentType(incidentType);
    }
  }

  /** Probably the worst way to do this */
  private void getUnitsArray(String unitString) {
    ArrayList<String> unitsArray = new ArrayList<>();
    Scanner scanner = new Scanner(unitString);
    scanner.useDelimiter(",");

    // go through and get every unit within the list
    while (scanner.hasNext()) {
      String unit = scanner.next();
      unitsArray.add(unit.trim());
    }

    // copy contents
    if (unitsArray.size() != 0) {
      this.units = new String[unitsArray.size()];
      for (int x = 0; x < this.units.length; x++) {
        //System.out.println(x + ": " + unitsArray.get(x));
        this.units[x] = unitsArray.get(x);
      }
    }
  }

  //getters and setters which will most likely never be used but just in case
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
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

  public String getComment() {
    return comment;
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
    this.incidentType = incidentType;
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

  private String getUnitsString() {
    String units = "";

    // hopefully fix crashing issue by not returning some sort of null pointer
    if (this.units != null && this.units.length != 0) {

      for (int x = 0; x < this.units.length; x++) {
        if (x != this.units.length - 1) {
          units += this.units[x] + ", ";
        } else {
          units += this.units[x];
        }
      }
    }

    return units;
  }

  /**
   * Get the block and the street name as a single String
   *
   * @return The full street address of this Incident
   */
  public String getStreetAddress() {
    return this.block + " " + this.address + ", " + this.city;
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

    if (this.block.equals("")) {
      return this.responseDate
          + "\nIncident Number: "
          + this.incidentNumber
          + "\nAddress: "
          + this.address
          + "\nCity: "
          + this.city
          + "\nUnits: "
          + this.getUnitsString()
          + "\nStatus: "
          + this.status;
    } else {
      return this.responseDate
          + "\nIncident Number: "
          + this.incidentNumber
          + "\nAddress: "
          + this.block
          + " "
          + this.address
          + "\nCity: "
          + this.city
          + "\nUnits: "
          + this.getUnitsString()
          + "\nStatus: "
          + this.status;
    }
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
  }
}
