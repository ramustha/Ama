
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class TwitterEntities implements Serializable {
  @SerializedName("urls")
  private List<Url> urls;
  @SerializedName("hashtags")
  private List<Hashtag> hashtags;
  @SerializedName("user_mentions")
  private List<Object> userMentions;
  @SerializedName("symbols")
  private List<Object> symbols;

  public List<Url> getUrls()
  {
    return urls;
  }

  public void setUrls(List<Url> urls)
  {
    this.urls = urls;
  }

  public List<Hashtag> getHashtags()
  {
    return hashtags;
  }

  public void setHashtags(List<Hashtag> hashtags)
  {
    this.hashtags = hashtags;
  }

  public List<Object> getUserMentions()
  {
    return userMentions;
  }

  public void setUserMentions(List<Object> userMentions)
  {
    this.userMentions = userMentions;
  }

  public List<Object> getSymbols()
  {
    return symbols;
  }

  public void setSymbols(List<Object> symbols)
  {
    this.symbols = symbols;
  }

  @Override public String toString()
  {
    return "TwitterEntities{" +
        "urls=" + urls +
        ", hashtags=" + hashtags +
        ", userMentions=" + userMentions +
        ", symbols=" + symbols +
        '}';
  }
}
