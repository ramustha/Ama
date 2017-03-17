
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Message2 implements Serializable {
  @SerializedName("postedTime")
  private String postedTime;
  @SerializedName("verb")
  private String verb;
  @SerializedName("link")
  private String link;
  @SerializedName("generator")
  private Generator generator;
  @SerializedName("body")
  private String body;
  @SerializedName("favoritesCount")
  private int favoritesCount;
  @SerializedName("objectType")
  private String objectType;
  @SerializedName("actor")
  private Actor actor;
  @SerializedName("provider")
  private Provider provider;
  @SerializedName("twitter_filter_level")
  private String twitterFilterLevel;
  @SerializedName("twitter_entities")
  private TwitterEntities twitterEntities;
  @SerializedName("twitter_lang")
  private String twitterLang;
  @SerializedName("id")
  private String id;
  @SerializedName("retweetCount")
  private int retweetCount;
  @SerializedName("gnip")
  private Gnip gnip;
  @SerializedName("object")
  private Object object;

  public String getPostedTime()
  {
    return postedTime;
  }

  public void setPostedTime(String postedTime)
  {
    this.postedTime = postedTime;
  }

  public String getVerb()
  {
    return verb;
  }

  public void setVerb(String verb)
  {
    this.verb = verb;
  }

  public String getLink()
  {
    return link;
  }

  public void setLink(String link)
  {
    this.link = link;
  }

  public Generator getGenerator()
  {
    return generator;
  }

  public void setGenerator(Generator generator)
  {
    this.generator = generator;
  }

  public String getBody()
  {
    return body;
  }

  public void setBody(String body)
  {
    this.body = body;
  }

  public int getFavoritesCount()
  {
    return favoritesCount;
  }

  public void setFavoritesCount(int favoritesCount)
  {
    this.favoritesCount = favoritesCount;
  }

  public String getObjectType()
  {
    return objectType;
  }

  public void setObjectType(String objectType)
  {
    this.objectType = objectType;
  }

  public Actor getActor()
  {
    return actor;
  }

  public void setActor(Actor actor)
  {
    this.actor = actor;
  }

  public Provider getProvider()
  {
    return provider;
  }

  public void setProvider(Provider provider)
  {
    this.provider = provider;
  }

  public String getTwitterFilterLevel()
  {
    return twitterFilterLevel;
  }

  public void setTwitterFilterLevel(String twitterFilterLevel)
  {
    this.twitterFilterLevel = twitterFilterLevel;
  }

  public TwitterEntities getTwitterEntities()
  {
    return twitterEntities;
  }

  public void setTwitterEntities(TwitterEntities twitterEntities)
  {
    this.twitterEntities = twitterEntities;
  }

  public String getTwitterLang()
  {
    return twitterLang;
  }

  public void setTwitterLang(String twitterLang)
  {
    this.twitterLang = twitterLang;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public int getRetweetCount()
  {
    return retweetCount;
  }

  public void setRetweetCount(int retweetCount)
  {
    this.retweetCount = retweetCount;
  }

  public Gnip getGnip()
  {
    return gnip;
  }

  public void setGnip(Gnip gnip)
  {
    this.gnip = gnip;
  }

  public Object getObject()
  {
    return object;
  }

  public void setObject(Object object)
  {
    this.object = object;
  }

  @Override public String toString()
  {
    return "Message{" +
        "postedTime='" + postedTime + '\'' +
        ", verb='" + verb + '\'' +
        ", link='" + link + '\'' +
        ", generator=" + generator +
        ", body='" + body + '\'' +
        ", favoritesCount=" + favoritesCount +
        ", objectType='" + objectType + '\'' +
        ", actor=" + actor +
        ", provider=" + provider +
        ", twitterFilterLevel='" + twitterFilterLevel + '\'' +
        ", twitterEntities=" + twitterEntities +
        ", twitterLang='" + twitterLang + '\'' +
        ", id='" + id + '\'' +
        ", retweetCount=" + retweetCount +
        ", gnip=" + gnip +
        ", object=" + object +
        '}';
  }
}
