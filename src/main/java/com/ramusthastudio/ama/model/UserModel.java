package com.ramusthastudio.ama.model;

public class UserModel {
  private long id;
  private String name;
  private String screenName;
  private String location;
  private String description;
  private String profileImageUrl;
  private String originalProfileImageUrlHttp;
  private String originalProfileImageUrlHttps;
  private boolean isProtected;
  private int followersCount;
  private String status;
  private int friendsCount;
  private boolean isVerified;

  public UserModel() { }

  public UserModel(long aId, String aName, String aScreenName, String aLocation, String aDescription, String aProfileImageUrl, String aOriginalProfileImageUrlHttp, String aOriginalProfileImageUrlHttps, boolean aIsProtected, int aFollowersCount, String aStatus, int aFriendsCount, boolean aIsVerified) {
    id = aId;
    name = aName;
    screenName = aScreenName;
    location = aLocation;
    description = aDescription;
    profileImageUrl = aProfileImageUrl;
    originalProfileImageUrlHttp = aOriginalProfileImageUrlHttp;
    originalProfileImageUrlHttps = aOriginalProfileImageUrlHttps;
    isProtected = aIsProtected;
    followersCount = aFollowersCount;
    status = aStatus;
    friendsCount = aFriendsCount;
    isVerified = aIsVerified;
  }

  public long getId() { return id; }
  public String getName() { return name; }
  public String getScreenName() { return screenName; }
  public String getLocation() { return location; }
  public String getDescription() { return description; }
  public String getProfileImageUrl() { return profileImageUrl; }
  public String getOriginalProfileImageUrlHttp() { return originalProfileImageUrlHttp; }
  public String getOriginalProfileImageUrlHttps() { return originalProfileImageUrlHttps; }
  public boolean isProtected() { return isProtected; }
  public int getFollowersCount() { return followersCount; }
  public String getStatus() { return status; }
  public int getFriendsCount() { return friendsCount; }
  public boolean isVerified() { return isVerified; }

  public UserModel setId(long aId) {
    id = aId;
    return this;
  }
  public UserModel setName(String aName) {
    name = aName;
    return this;
  }
  public UserModel setScreenName(String aScreenName) {
    screenName = aScreenName;
    return this;
  }
  public UserModel setLocation(String aLocation) {
    location = aLocation;
    return this;
  }
  public UserModel setDescription(String aDescription) {
    description = aDescription;
    return this;
  }
  public UserModel setProfileImageUrl(String aProfileImageUrl) {
    profileImageUrl = aProfileImageUrl;
    return this;
  }
  public UserModel setOriginalProfileImageUrlHttp(String aOriginalProfileImageUrlHttp) {
    originalProfileImageUrlHttp = aOriginalProfileImageUrlHttp;
    return this;
  }
  public UserModel setOriginalProfileImageUrlHttps(String aOriginalProfileImageUrlHttps) {
    originalProfileImageUrlHttps = aOriginalProfileImageUrlHttps;
    return this;
  }
  public UserModel setProtected(boolean aProtected) {
    isProtected = aProtected;
    return this;
  }
  public UserModel setFollowersCount(int aFollowersCount) {
    followersCount = aFollowersCount;
    return this;
  }
  public UserModel setStatus(String aStatus) {
    status = aStatus;
    return this;
  }
  public UserModel setFriendsCount(int aFriendsCount) {
    friendsCount = aFriendsCount;
    return this;
  }
  public UserModel setVerified(boolean aVerified) {
    isVerified = aVerified;
    return this;
  }

  @Override public String toString() {
    return "UserModel{" +
        "id=" + id + "\n" +
        ", name='" + name + "\n" +
        ", screenName='" + screenName + "\n" +
        ", location='" + location + "\n" +
        ", description='" + description + "\n" +
        ", profileImageUrl='" + profileImageUrl + "\n" +
        ", originalProfileImageUrlHttp='" + originalProfileImageUrlHttp + "\n" +
        ", originalProfileImageUrlHttps='" + originalProfileImageUrlHttps + "\n" +
        ", isProtected=" + isProtected + "\n" +
        ", followersCount=" + followersCount + "\n" +
        ", status='" + status + "\n" +
        ", friendsCount=" + friendsCount + "\n" +
        ", isVerified=" + isVerified + "\n" +
        '}';
  }
}
