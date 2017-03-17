package com.ramusthastudio.ama.model;

public class UserLine {
  private String displayName;
  private String userId;
  private String pictureUrl;
  private String statusMessage;

  public UserLine(String aDisplayName, String aUserId, String aPictureUrl, String aStatusMessage) {
    displayName = aDisplayName;
    userId = aUserId;
    pictureUrl = aPictureUrl;
    statusMessage = aStatusMessage;
  }

  public String getDisplayName() { return displayName; }
  public String getUserId() { return userId; }
  public String getPictureUrl() { return pictureUrl; }
  public String getStatusMessage() { return statusMessage; }

  public UserLine setDisplayName(String aDisplayName) {
    displayName = aDisplayName;
    return this;
  }
  public UserLine setUserId(String aUserId) {
    userId = aUserId;
    return this;
  }
  public UserLine setPictureUrl(String aPictureUrl) {
    pictureUrl = aPictureUrl;
    return this;
  }
  public UserLine setStatusMessage(String aStatusMessage) {
    statusMessage = aStatusMessage;
    return this;
  }

  @Override public String toString() {
    return "UserLine{" +
        "displayName='" + displayName + "\n" +
        ", userId='" + userId + "\n" +
        ", pictureUrl='" + pictureUrl + "\n" +
        ", statusMessage='" + statusMessage + "\n" +
        '}';
  }
}
