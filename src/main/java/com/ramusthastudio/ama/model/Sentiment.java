
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Sentiment implements Serializable {
  @SerializedName("evidence")
  private List<Evidence> evidence;
  @SerializedName("polarity")
  private String polarity;

  public List<Evidence> getEvidence()
  {
    return evidence;
  }

  public void setEvidence(List<Evidence> evidence)
  {
    this.evidence = evidence;
  }

  public String getPolarity()
  {
    return polarity;
  }

  public void setPolarity(String polarity)
  {
    this.polarity = polarity;
  }

  @Override public String toString()
  {
    return "Sentiment{" +
        "evidence=" + evidence +
        ", polarity='" + polarity + '\'' +
        '}';
  }
}
