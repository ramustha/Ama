
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Cde implements Serializable {
  @SerializedName("author")
  private Author author;
  @SerializedName("content")
  private Content content;

  public Author getAuthor()
  {
    return author;
  }

  public void setAuthor(Author author)
  {
    this.author = author;
  }

  public Content getContent()
  {
    return content;
  }

  public void setContent(Content content)
  {
    this.content = content;
  }

  @Override public String toString()
  {
    return "Cde{" +
        "author=" + author +
        ", content=" + content +
        '}';
  }
}
