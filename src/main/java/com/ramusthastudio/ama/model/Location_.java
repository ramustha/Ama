
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Location_ implements Serializable {
  @SerializedName("displayName")
  private String displayName;
  @SerializedName("objectType")
  private String objectType;

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
    return "Location_{" +
        "displayName='" + displayName + '\'' +
        ", objectType='" + objectType + '\'' +
        '}';
  }
}
