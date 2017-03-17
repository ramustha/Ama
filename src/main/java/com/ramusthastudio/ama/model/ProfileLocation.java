
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ProfileLocation implements Serializable {
  @SerializedName("geo")
  private Geo geo;
  @SerializedName("address")
  private Address address;
  @SerializedName("displayName")
  private String displayName;
  @SerializedName("objectType")
  private String objectType;

  public Geo getGeo()
  {
    return geo;
  }

  public void setGeo(Geo geo)
  {
    this.geo = geo;
  }

  public Address getAddress()
  {
    return address;
  }

  public void setAddress(Address address)
  {
    this.address = address;
  }

  public String getDisplayName()
  {
    return displayName;
  }

  public void setDisplayName(String displayName)
  {
    this.displayName = displayName;
  }

  public String getObjectType()
  {
    return objectType;
  }

  public void setObjectType(String objectType)
  {
    this.objectType = objectType;
  }

  @Override public String toString()
  {
    return "ProfileLocation{" +
        "geo=" + geo +
        ", address=" + address +
        ", displayName='" + displayName + '\'' +
        ", objectType='" + objectType + '\'' +
        '}';
  }
}
