
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Content implements Serializable {
  @SerializedName("sentiment")
  private Sentiment sentiment;

  public Sentiment getSentiment()
  {
    return sentiment;
  }

  public void setSentiment(Sentiment sentiment)
  {
    this.sentiment = sentiment;
  }

  @Override public String toString()
  {
    return "Content{" +
        "sentiment=" + sentiment +
        '}';
  }
}
