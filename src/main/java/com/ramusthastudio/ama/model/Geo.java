
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Geo implements Serializable {
  @SerializedName("coordinates")
  private List<Float> coordinates;
  @SerializedName("type")
  private String type;

  public List<Float> getCoordinates()
  {
    return coordinates;
  }

  public void setCoordinates(List<Float> coordinates)
  {
    this.coordinates = coordinates;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  @Override public String toString()
  {
    return "Geo{" +
        "coordinates=" + coordinates +
        ", type='" + type + '\'' +
        '}';
  }
}
