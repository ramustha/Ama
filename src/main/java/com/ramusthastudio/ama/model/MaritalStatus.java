
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class MaritalStatus implements Serializable {
  @SerializedName("isMarried")
  private String isMarried;
  @SerializedName("evidence")
  private String evidence;

  public String getIsMarried()
  {
    return isMarried;
  }

  public void setIsMarried(String isMarried)
  {
    this.isMarried = isMarried;
  }

  public String getEvidence()
  {
    return evidence;
  }

  public void setEvidence(String evidence)
  {
    this.evidence = evidence;
  }

  @Override public String toString()
  {
    return "MaritalStatus{" +
        "isMarried='" + isMarried + '\'' +
        ", evidence='" + evidence + '\'' +
        '}';
  }
}
