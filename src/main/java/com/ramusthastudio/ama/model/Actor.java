
package com.ramusthastudio.ama.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Actor implements Serializable {
  @SerializedName("summary")
  private String summary;
  @SerializedName("image")
  private String image;
  @SerializedName("statusesCount")
  private int statusesCount;
  @SerializedName("utcOffset")
  private String utcOffset;
  @SerializedName("languages")
  private List<String> languages;
  @SerializedName("preferredUsername")
  private String preferredUsername;
  @SerializedName("displayName")
  private String displayName;
  @SerializedName("postedTime")
  private String postedTime;
  @SerializedName("link")
  private String link;
  @SerializedName("verified")
  private boolean verified;
  @SerializedName("friendsCount")
  private int friendsCount;
  @SerializedName("twitterTimeZone")
  private String twitterTimeZone;
  @SerializedName("favoritesCount")
  private int favoritesCount;
  @SerializedName("listedCount")
  private int listedCount;
  @SerializedName("objectType")
  private String objectType;
  @SerializedName("links")
  private List<Link> links;
  @SerializedName("location")
  private Location_ location;
  @SerializedName("id")
  private String id;
  @SerializedName("followersCount")
  private int followersCount;

  public String getSummary()
  {
    return summary;
  }

  public void setSummary(String summary)
  {
    this.summary = summary;
  }

  public String getImage()
  {
    return image;
  }

  public void setImage(String image)
  {
    this.image = image;
  }

  public int getStatusesCount()
  {
    return statusesCount;
  }

  public void setStatusesCount(int statusesCount)
  {
    this.statusesCount = statusesCount;
  }

  public String getUtcOffset()
  {
    return utcOffset;
  }

  public void setUtcOffset(String utcOffset)
  {
    this.utcOffset = utcOffset;
  }

  public List<String> getLanguages()
  {
    return languages;
  }

  public void setLanguages(List<String> languages)
  {
    this.languages = languages;
  }

  public String getPreferredUsername()
  {
    return preferredUsername;
  }

  public void setPreferredUsername(String preferredUsername)
  {
    this.preferredUsername = preferredUsername;
  }

  public String getDisplayName()
  {
    return displayName;
  }

  public void setDisplayName(String displayName)
  {
    this.displayName = displayName;
  }

  public String getPostedTime()
  {
    return postedTime;
  }

  public void setPostedTime(String postedTime)
  {
    this.postedTime = postedTime;
  }

  public String getLink()
  {
    return link;
  }

  public void setLink(String link)
  {
    this.link = link;
  }

  public boolean isVerified()
  {
    return verified;
  }

  public void setVerified(boolean verified)
  {
    this.verified = verified;
  }

  public int getFriendsCount()
  {
    return friendsCount;
  }

  public void setFriendsCount(int friendsCount)
  {
    this.friendsCount = friendsCount;
  }

  public String getTwitterTimeZone()
  {
    return twitterTimeZone;
  }

  public void setTwitterTimeZone(String twitterTimeZone)
  {
    this.twitterTimeZone = twitterTimeZone;
  }

  public int getFavoritesCount()
  {
    return favoritesCount;
  }

  public void setFavoritesCount(int favoritesCount)
  {
    this.favoritesCount = favoritesCount;
  }

  public int getListedCount()
  {
    return listedCount;
  }

  public void setListedCount(int listedCount)
  {
    this.listedCount = listedCount;
  }

  public String getObjectType()
  {
    return objectType;
  }

  public void setObjectType(String objectType)
  {
    this.objectType = objectType;
  }

  public List<Link> getLinks()
  {
    return links;
  }

  public void setLinks(List<Link> links)
  {
    this.links = links;
  }

  public Location_ getLocation()
  {
    return location;
  }

  public void setLocation(Location_ location)
  {
    this.location = location;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public int getFollowersCount()
  {
    return followersCount;
  }

  public void setFollowersCount(int followersCount)
  {
    this.followersCount = followersCount;
  }

  @Override public String toString()
  {
    return "Actor{" +
        "summary=" + summary +
        ", image='" + image + '\'' +
        ", statusesCount=" + statusesCount +
        ", utcOffset=" + utcOffset +
        ", languages=" + languages +
        ", preferredUsername='" + preferredUsername + '\'' +
        ", displayName='" + displayName + '\'' +
        ", postedTime='" + postedTime + '\'' +
        ", link='" + link + '\'' +
        ", verified=" + verified +
        ", friendsCount=" + friendsCount +
        ", twitterTimeZone=" + twitterTimeZone +
        ", favoritesCount=" + favoritesCount +
        ", listedCount=" + listedCount +
        ", objectType='" + objectType + '\'' +
        ", links=" + links +
        ", location=" + location +
        ", id='" + id + '\'' +
        ", followersCount=" + followersCount +
        '}';
  }
}
