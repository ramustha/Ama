package com.ramusthastudio.ama.model;

public class UserChat {
  private String userId;
  private String lastChat;
  private long lastTime;

  public UserChat(String aUserId, String aLastChat, long aLastTime) {
    userId = aUserId;
    lastChat = aLastChat;
    lastTime = aLastTime;
  }

  public String getUserId() { return userId; }
  public String getLastChat() { return lastChat; }
  public long getLastTime() { return lastTime; }

  public UserChat setUserId(String aUserId) {
    userId = aUserId;
    return this;
  }
  public UserChat setLastChat(String aLastChat) {
    lastChat = aLastChat;
    return this;
  }
  public UserChat setLastTime(long aLastTime) {
    lastTime = aLastTime;
    return this;
  }

  @Override public String toString() {
    return "UserChat{" +
        "userId='" + userId + "\n" +
        ", lastChat='" + lastChat + "\n" +
        ", lastTime=" + lastTime + "\n" +
        '}';
  }
}
