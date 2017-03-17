
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Tweet implements Serializable {
  @SerializedName("cde")
  private Cde cde;
  @SerializedName("cdeInternal")
  private CdeInternal cdeInternal;
  @SerializedName("message")
  private Message2 message;

  public Cde getCde()
  {
    return cde;
  }

  public void setCde(Cde cde)
  {
    this.cde = cde;
  }

  public CdeInternal getCdeInternal()
  {
    return cdeInternal;
  }

  public void setCdeInternal(CdeInternal cdeInternal)
  {
    this.cdeInternal = cdeInternal;
  }

  public Message2 getMessage()
  {
    return message;
  }

  public void setMessage(Message2 message)
  {
    this.message = message;
  }

  @Override public String toString()
  {
    return "Tweet{" +
        "cde=" + cde +
        ", cdeInternal=" + cdeInternal +
        ", message=" + message +
        '}';
  }
}
