
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Url_ implements Serializable {
  @SerializedName("expanded_url")
  private String expandedUrl;
  @SerializedName("expanded_status")
  private int expandedStatus;
  @SerializedName("expanded_url_title")
  private String expandedUrlTitle;
  @SerializedName("expanded_url_description")
  private String expandedUrlDescription;
  @SerializedName("url")
  private String url;

  public String getExpandedUrl()
  {
    return expandedUrl;
  }

  public void setExpandedUrl(String expandedUrl)
  {
    this.expandedUrl = expandedUrl;
  }

  public int getExpandedStatus()
  {
    return expandedStatus;
  }

  public void setExpandedStatus(int expandedStatus)
  {
    this.expandedStatus = expandedStatus;
  }

  public String getExpandedUrlTitle()
  {
    return expandedUrlTitle;
  }

  public void setExpandedUrlTitle(String expandedUrlTitle)
  {
    this.expandedUrlTitle = expandedUrlTitle;
  }

  public String getExpandedUrlDescription()
  {
    return expandedUrlDescription;
  }

  public void setExpandedUrlDescription(String expandedUrlDescription)
  {
    this.expandedUrlDescription = expandedUrlDescription;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  @Override public String toString()
  {
    return "Url_{" +
        "expandedUrl='" + expandedUrl + '\'' +
        ", expandedStatus=" + expandedStatus +
        ", expandedUrlTitle='" + expandedUrlTitle + '\'' +
        ", expandedUrlDescription='" + expandedUrlDescription + '\'' +
        ", url='" + url + '\'' +
        '}';
  }
}
