
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Hashtag implements Serializable {
  @SerializedName("indices")
  private List<Integer> indices;
  @SerializedName("text")
  private String text;

  public List<Integer> getIndices()
  {
    return indices;
  }

  public void setIndices(List<Integer> indices)
  {
    this.indices = indices;
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    this.text = text;
  }

  @Override public String toString()
  {
    return "Hashtag{" +
        "indices=" + indices +
        ", text='" + text + '\'' +
        '}';
  }
}
