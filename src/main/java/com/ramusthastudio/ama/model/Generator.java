
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Generator implements Serializable {
  @SerializedName("displayName")
  private String displayName;
  @SerializedName("link")
  private String link;

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

  @Override public String toString()
  {
    return "Generator{" +
        "displayName='" + displayName + '\'' +
        ", link='" + link + '\'' +
        '}';
  }
}
