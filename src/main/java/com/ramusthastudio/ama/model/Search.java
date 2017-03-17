
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Search implements Serializable {
  @SerializedName("results")
  private int results;

  @SerializedName("current")
  private int current;

  public int getResults()
  {
    return results;
  }

  public void setResults(int results)
  {
    this.results = results;
  }

  public int getCurrent()
  {
    return current;
  }

  public void setCurrent(int current)
  {
    this.current = current;
  }

  @Override public String toString()
  {
    return "Search{" +
        "results=" + results +
        ", current=" + current +
        '}';
  }
}
