
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Parenthood implements Serializable {
  @SerializedName("isParent")
  private String isParent;
  @SerializedName("evidence")
  private String evidence;

  public String getIsParent()
  {
    return isParent;
  }

  public void setIsParent(String isParent)
  {
    this.isParent = isParent;
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
    return "Parenthood{" +
        "isParent='" + isParent + '\'' +
        ", evidence='" + evidence + '\'' +
        '}';
  }
}
