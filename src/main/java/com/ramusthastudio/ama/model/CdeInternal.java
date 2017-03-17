
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class CdeInternal implements Serializable {
  @SerializedName("tracks")
  private List<Track> tracks;

  public List<Track> getTracks()
  {
    return tracks;
  }

  public void setTracks(List<Track> tracks)
  {
    this.tracks = tracks;
  }

  @Override public String toString()
  {
    return "CdeInternal{" +
        "tracks=" + tracks +
        '}';
  }
}
