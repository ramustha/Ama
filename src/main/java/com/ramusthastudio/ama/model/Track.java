
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Track implements Serializable {
  @SerializedName("id")
  private String id;

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  @Override public String toString()
  {
    return "Track{" +
        "id='" + id + '\'' +
        '}';
  }
}
