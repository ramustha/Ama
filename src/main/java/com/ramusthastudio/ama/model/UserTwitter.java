package com.ramusthastudio.ama.model;

public class UserTwitter {
  private String id;
  private String username;
  private String displayName;
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

  public UserTwitter() { }

  public UserTwitter(String aId, String aUsername, String aDisplayName, String aLocation, String aDescription, String aProfileImageUrl, String aOriginalProfileImageUrlHttp, String aOriginalProfileImageUrlHttps, boolean aIsProtected, int aFollowersCount, String aStatus, int aFriendsCount, boolean aIsVerified) {
    id = aId;
    username = aUsername;
    displayName = aDisplayName;
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

  public String getId() { return id; }
  public String getUsername() { return username; }
  public String getDisplayName() { return displayName; }
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

  public UserTwitter setId(String aId) {
    id = aId;
    return this;
  }
  public UserTwitter setUsername(String aUsername) {
    username = aUsername;
    return this;
  }
  public UserTwitter setDisplayName(String aDisplayName) {
    displayName = aDisplayName;
    return this;
  }
  public UserTwitter setLocation(String aLocation) {
    location = aLocation;
    return this;
  }
  public UserTwitter setDescription(String aDescription) {
    description = aDescription;
    return this;
  }
  public UserTwitter setProfileImageUrl(String aProfileImageUrl) {
    profileImageUrl = aProfileImageUrl;
    return this;
  }
  public UserTwitter setOriginalProfileImageUrlHttp(String aOriginalProfileImageUrlHttp) {
    originalProfileImageUrlHttp = aOriginalProfileImageUrlHttp;
    return this;
  }
  public UserTwitter setOriginalProfileImageUrlHttps(String aOriginalProfileImageUrlHttps) {
    originalProfileImageUrlHttps = aOriginalProfileImageUrlHttps;
    return this;
  }
  public UserTwitter setProtected(boolean aProtected) {
    isProtected = aProtected;
    return this;
  }
  public UserTwitter setFollowersCount(int aFollowersCount) {
    followersCount = aFollowersCount;
    return this;
  }
  public UserTwitter setStatus(String aStatus) {
    status = aStatus;
    return this;
  }
  public UserTwitter setFriendsCount(int aFriendsCount) {
    friendsCount = aFriendsCount;
    return this;
  }
  public UserTwitter setVerified(boolean aVerified) {
    isVerified = aVerified;
    return this;
  }

  @Override public String toString() {
    return "UserModel{" +
        "id=" + id + "\n" +
        ", username='" + username + "\n" +
        ", displayName='" + displayName + "\n" +
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
