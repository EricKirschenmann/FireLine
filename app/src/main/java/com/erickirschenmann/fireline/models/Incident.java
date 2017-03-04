package com.erickirschenmann.fireline.models;

import java.util.Arrays;

/** Created by eric on 3/4/17. */
class Incident {

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
      String[] units) {
    this.address = address;
    this.block = block;
    this.city = city;
    this.comment = comment;
    this.incidentNumber = incidentNumber;
    this.incidentType = incidentType;
    this.latitude = latitude;
    this.longitude = longitude;
    this.responseDate = responseDate;
    this.status = status;
    this.units = units;
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

  public void setIncidentType(String incidentType) {
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
    return "Incident{"
        + "address='"
        + address
        + '\''
        + ", block='"
        + block
        + '\''
        + ", city='"
        + city
        + '\''
        + ", comment='"
        + comment
        + '\''
        + ", incidentNumber='"
        + incidentNumber
        + '\''
        + ", incidentType='"
        + incidentType
        + '\''
        + ", latitude="
        + latitude
        + ", longitude="
        + longitude
        + ", responseDate='"
        + responseDate
        + '\''
        + ", status='"
        + status
        + '\''
        + ", units="
        + Arrays.toString(units)
        + '}';
  }
}
