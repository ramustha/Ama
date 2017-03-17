
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Provider implements Serializable {
  @SerializedName("displayName")
  private String displayName;
  @SerializedName("link")
  private String link;
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

  public String getLink()
  {
    return link;
  }

  public void setLink(String link)
  {
    this.link = link;
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
    return "Provider{" +
        "displayName='" + displayName + '\'' +
        ", link='" + link + '\'' +
        ", objectType='" + objectType + '\'' +
        '}';
  }
}
