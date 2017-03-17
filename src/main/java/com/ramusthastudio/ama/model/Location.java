
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Location implements Serializable {
  @SerializedName("country")
  private String country;
  @SerializedName("city")
  private String city;
  @SerializedName("state")
  private String state;

  public String getCountry()
  {
    return country;
  }

  public void setCountry(String country)
  {
    this.country = country;
  }

  public String getCity()
  {
    return city;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  public String getState()
  {
    return state;
  }

  public void setState(String state)
  {
    this.state = state;
  }

  @Override public String toString()
  {
    return "Location{" +
        "country='" + country + '\'' +
        ", city='" + city + '\'' +
        ", state='" + state + '\'' +
        '}';
  }
}
