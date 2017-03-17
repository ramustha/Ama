
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class ApiTweets implements Serializable {
  private String fTitle;

  @SerializedName("search")
  private Search search;
  @SerializedName("tweets")
  private List<Tweet> tweets;
  @SerializedName("related")
  private Related related;

  public String getTitle() { return fTitle; }

  public void setTitle(String aTitle) { fTitle = aTitle; }

  public Search getSearch()
  {
    return search;
  }

  public void setSearch(Search search)
  {
    this.search = search;
  }

  public List<Tweet> getTweets()
  {
    return tweets;
  }

  public void setTweets(List<Tweet> tweets)
  {
    this.tweets = tweets;
  }

  public Related getRelated()
  {
    return related;
  }

  public void setRelated(Related related)
  {
    this.related = related;
  }

  @Override public String toString() {
    return "ApiTweets{" +
        "fTitle='" + fTitle + '\'' +
        ", search=" + search +
        ", tweets=" + tweets +
        ", related=" + related +
        '}';
  }
}
