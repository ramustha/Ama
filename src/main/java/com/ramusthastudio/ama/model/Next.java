
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Next implements Serializable {
  @SerializedName("href")
  private String href;

  public String getHref()
  {
    return href;
  }

  public void setHref(String href)
  {
    this.href = href;
  }

  @Override public String toString()
  {
    return "Next{" +
        "href='" + href + '\'' +
        '}';
  }
}
