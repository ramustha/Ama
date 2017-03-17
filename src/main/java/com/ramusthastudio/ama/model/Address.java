
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Address implements Serializable {
  @SerializedName("country")
  private String country;
  @SerializedName("countryCode")
  private String countryCode;
  @SerializedName("locality")
  private String locality;
  @SerializedName("region")
  private String region;

  public String getCountry()
  {
    return country;
  }

  public void setCountry(String country)
  {
    this.country = country;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode(String countryCode)
  {
    this.countryCode = countryCode;
  }

  public String getLocality()
  {
    return locality;
  }

  public void setLocality(String locality)
  {
    this.locality = locality;
  }

  public String getRegion()
  {
    return region;
  }

  public void setRegion(String region)
  {
    this.region = region;
  }

  @Override public String toString()
  {
    return "Address{" +
        "country='" + country + '\'' +
        ", countryCode='" + countryCode + '\'' +
        ", locality='" + locality + '\'' +
        ", region='" + region + '\'' +
        '}';
  }
}
