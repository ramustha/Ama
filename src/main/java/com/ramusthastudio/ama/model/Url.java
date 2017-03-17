
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Url implements Serializable {
  @SerializedName("display_url")
  private String displayUrl;
  @SerializedName("indices")
  private List<Integer> indices;
  @SerializedName("expanded_url")
  private String expandedUrl;
  @SerializedName("url")
  private String url;

  public String getDisplayUrl()
  {
    return displayUrl;
  }

  public void setDisplayUrl(String displayUrl)
  {
    this.displayUrl = displayUrl;
  }

  public List<Integer> getIndices()
  {
    return indices;
  }

  public void setIndices(List<Integer> indices)
  {
    this.indices = indices;
  }

  public String getExpandedUrl()
  {
    return expandedUrl;
  }

  public void setExpandedUrl(String expandedUrl)
  {
    this.expandedUrl = expandedUrl;
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
    return "Url{" +
        "displayUrl='" + displayUrl + '\'' +
        ", indices=" + indices +
        ", expandedUrl='" + expandedUrl + '\'' +
        ", url='" + url + '\'' +
        '}';
  }
}
