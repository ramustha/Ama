
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Gnip implements Serializable {
  @SerializedName("urls")
  private List<Url_> urls;
  @SerializedName("profileLocations")
  private List<ProfileLocation> profileLocations = null;

  public List<Url_> getUrls()
  {
    return urls;
  }

  public void setUrls(List<Url_> urls)
  {
    this.urls = urls;
  }

  public List<ProfileLocation> getProfileLocations()
  {
    return profileLocations;
  }

  public void setProfileLocations(List<ProfileLocation> profileLocations)
  {
    this.profileLocations = profileLocations;
  }

  @Override public String toString()
  {
    return "Gnip{" +
        "urls=" + urls +
        ", profileLocations=" + profileLocations +
        '}';
  }
}
