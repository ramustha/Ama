
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Author implements Serializable {
  @SerializedName("gender")
  private String gender;
  @SerializedName("parenthood")
  private Parenthood parenthood;
  @SerializedName("location")
  private Location location;
  @SerializedName("maritalStatus")
  private MaritalStatus maritalStatus;

  public String getGender()
  {
    return gender;
  }

  public void setGender(String gender)
  {
    this.gender = gender;
  }

  public Parenthood getParenthood()
  {
    return parenthood;
  }

  public void setParenthood(Parenthood parenthood)
  {
    this.parenthood = parenthood;
  }

  public Location getLocation()
  {
    return location;
  }

  public void setLocation(Location location)
  {
    this.location = location;
  }

  public MaritalStatus getMaritalStatus()
  {
    return maritalStatus;
  }

  public void setMaritalStatus(MaritalStatus maritalStatus)
  {
    this.maritalStatus = maritalStatus;
  }

  @Override public String toString()
  {
    return "Author{" +
        "gender='" + gender + '\'' +
        ", parenthood=" + parenthood +
        ", location=" + location +
        ", maritalStatus=" + maritalStatus +
        '}';
  }
}
