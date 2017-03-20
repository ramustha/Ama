package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Evidence implements Serializable {
  @SerializedName("polarity")
  private String polarity;
  @SerializedName("sentimentTerm")
  private String sentimentTerm;

  private String id;
  private int size = 1;

  public Evidence(String aMessageId, String aPolarity, String aPolarityTerm, int aPolarityTermSize) {
    id = aMessageId;
    polarity = aPolarity;
    sentimentTerm = aPolarityTerm;
    size = aPolarityTermSize;
  }

  public String getId() { return id; }
  public void setId(String aId) { id = aId; }
  public Integer getSize() { return size; }
  public void setSize(int aSize) { size = aSize; }
  public String getPolarity() { return polarity; }
  public void setPolarity(String aPolarity)
  {
    polarity = aPolarity;
  }
  public String getSentimentTerm() { return sentimentTerm; }
  public void setSentimentTerm(String aSentimentTerm)
  {
    sentimentTerm = aSentimentTerm;
  }

  @Override public String toString()
  {
    return "Evidence{" +
        "polarity='" + polarity + '\'' +
        ", sentimentTerm='" + sentimentTerm + '\'' +
        ", id='" + id + '\'' +
        ", size=" + size +
        '}';
  }
}
