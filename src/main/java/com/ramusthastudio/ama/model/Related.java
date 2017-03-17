
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Related implements Serializable {
  @SerializedName("next")
  private Next next;

  public Next getNext()
  {
    return next;
  }

  public void setNext(Next next)
  {
    this.next = next;
  }

  @Override public String toString()
  {
    return "Related{" +
        "next=" + next +
        '}';
  }
}
