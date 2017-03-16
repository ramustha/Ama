package com.ramusthastudio.ama.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.common.base.Strings;
import java.io.StringWriter;
import java.io.Writer;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class Twitter4JHelper implements RateLimitStatusListener {
  private static final String TW_CONSUMER_KEY_PROP_NAME = "twitter.consumerKey";
  private static final String TW_CONSUMER_SECRET_PROP_NAME = "twitter.consumerSecret";
  private static final String TW_ACCESS_TOKEN_PROP_NAME = "twitter.accessToken";
  private static final String TW_ACCESS_SECRET_PROP_NAME = "twitter.accessSecret";

  Twitter twitter = null;

  boolean rateLimited = false;
  long rateLimitResetTime = -1;

  public Twitter4JHelper(Properties properties) throws Exception {
    String consumerKey = properties.getProperty(TW_CONSUMER_KEY_PROP_NAME);
    String consumerSecret = properties.getProperty(TW_CONSUMER_SECRET_PROP_NAME);
    String accessToken = properties.getProperty(TW_ACCESS_TOKEN_PROP_NAME);
    String accessSecret = properties.getProperty(TW_ACCESS_SECRET_PROP_NAME);
    // Validate that these are set and throw an error if they are not
    ArrayList<String> nullPropNames = new ArrayList<String>();
    if (Strings.isNullOrEmpty(consumerKey)) { nullPropNames.add(TW_CONSUMER_KEY_PROP_NAME); }
    if (Strings.isNullOrEmpty(consumerSecret)) { nullPropNames.add(TW_CONSUMER_SECRET_PROP_NAME); }
    if (Strings.isNullOrEmpty(accessToken)) { nullPropNames.add(TW_ACCESS_TOKEN_PROP_NAME); }
    if (Strings.isNullOrEmpty(accessSecret)) { nullPropNames.add(TW_ACCESS_SECRET_PROP_NAME); }
    if (nullPropNames.size() > 0) {
      throw new Exception(
          "Cannot load the twitter credentials from the properties. The properties "
              + " are null or empty");
    }
    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setDebugEnabled(true)
        .setOAuthConsumerKey(consumerKey)
        .setOAuthConsumerSecret(consumerSecret)
        .setOAuthAccessToken(accessToken)
        .setOAuthAccessTokenSecret(accessSecret);
    TwitterFactory tf = new TwitterFactory(cb.build());
    twitter = tf.getInstance();
    twitter.addRateLimitStatusListener(this);
  }

  public String getUserImage(Status status) {
    return status.getUser().getProfileImageURL();
  }

  public Twitter getTwitter() {
    return twitter;
  }
  public String convertTweetsToPIContentItems(List<Status> tweets) throws Exception {
    Writer content = new StringWriter();
    JsonFactory factory = new JsonFactory();
    JsonGenerator gen = factory.createGenerator(content);
    gen.writeStartObject();
    gen.writeArrayFieldStart("contentItems");

    if (tweets.size() > 0) {
      String userIdStr = Long.toString(tweets.get(0).getUser().getId());
      for (Status status : tweets) {
        // Add the tweet text to the contentItems
        gen.writeStartObject();
        gen.writeStringField("userid", userIdStr);
        gen.writeStringField("id", Long.toString(status.getId()));
        gen.writeStringField("sourceid", "twitter4j");
        gen.writeStringField("contenttype", "text/plain");
        gen.writeStringField("language", status.getLang());
        gen.writeStringField("content", status.getText().replaceAll("[^(\\x20-\\x7F)]*", ""));
        gen.writeNumberField("created", status.getCreatedAt().getTime());
        gen.writeBooleanField("reply", (status.getInReplyToScreenName() != null));
        gen.writeBooleanField("forward", status.isRetweet());
        gen.writeEndObject();
      }
    }
    gen.writeEndArray();
    gen.writeEndObject();
    gen.flush();

    return content.toString();
  }

  public ResponseList<User> searchUsers(String idOrHandle) throws Exception {
    return twitter.searchUsers(idOrHandle, 20);
  }

  public String profileImgUser(String idOrHandle) throws Exception {
    String path = "";
    if (idOrHandle.startsWith("@")) {
      // Check rate limit
      checkRateLimitAndThrow();
      User user = twitter.showUser(idOrHandle.substring(1));
      if (user == null) {
        throw new Exception("Handle " + idOrHandle + " is not a valid twitter handle.");
      }
      path = user.getOriginalProfileImageURL();
    } else if (!idOrHandle.startsWith("@")) {
      // Check rate limit
      checkRateLimitAndThrow();
      User user = twitter.showUser(idOrHandle);
      if (user == null) {
        throw new Exception("Handle " + idOrHandle + " is not a valid twitter handle.");
      }
      path = user.getOriginalProfileImageURL();
    }
    return path;
  }

  public long checkUsers(String idOrHandle) throws Exception {
    long userId = -1;
    if (idOrHandle.startsWith("@")) {
      // Check rate limit
      checkRateLimitAndThrow();
      User user = twitter.showUser(idOrHandle.substring(1));
      if (user == null) {
        throw new Exception("Handle " + idOrHandle + " is not a valid twitter handle.");
      }
      userId = user.getId();
    } else if (!idOrHandle.startsWith("@")) {
      // Check rate limit
      checkRateLimitAndThrow();
      User user = twitter.showUser(idOrHandle);
      if (user == null) {
        throw new Exception("Handle " + idOrHandle + " is not a valid twitter handle.");
      }
      userId = user.getId();
    } else {
      userId = Long.valueOf(idOrHandle);
    }
    return userId;
  }

  public List<Status> getTweets(String idOrHandle, Set<String> langs, int numberOfNonRetweets) throws Exception {
    List<Status> retval = new ArrayList<>();
    long userId = -1;
    if (idOrHandle.startsWith("@")) {
      // Check rate limit
      checkRateLimitAndThrow();
      User user = twitter.showUser(idOrHandle.substring(1));
      if (user == null) {
        throw new Exception("Handle " + idOrHandle + " is not a valid twitter handle.");
      }
      userId = user.getId();
    } else if (!idOrHandle.startsWith("@")) {
      // Check rate limit
      checkRateLimitAndThrow();
      User user = twitter.showUser(idOrHandle);
      if (user == null) {
        throw new Exception("Handle " + idOrHandle + " is not a valid twitter handle.");
      }
      userId = user.getId();
    } else {
      userId = Long.valueOf(idOrHandle);
    }

    long cursor = -1;
    Paging page = new Paging(1, 200);
    do {
      checkRateLimitAndThrow();
      ResponseList<Status> tweets = twitter.getUserTimeline(userId, page);
      if (tweets == null || tweets.size() == 0) break;
      for (int i = 0; i < tweets.size(); i++) {
        Status status = tweets.get(i);
        cursor = status.getId() - 1;

        // Ignore retweets
        if (status.isRetweet()) continue;
        // Language
        if (!langs.contains(status.getLang())) continue;
        retval.add(status);
        if (retval.size() >= numberOfNonRetweets) return retval;
      }
      page.maxId(cursor);
    } while (true);
    return retval;
  }

  public List<Status> getTweets(String idOrHandle, Paging paging) throws Exception {
    List<Status> retval = new ArrayList<>();
    long userId = -1;
    if (idOrHandle.startsWith("@")) {
      // Check rate limit
      checkRateLimitAndThrow();
      User user = twitter.showUser(idOrHandle.substring(1));
      if (user == null) {
        throw new Exception("Handle " + idOrHandle + " is not a valid twitter handle.");
      }
      userId = user.getId();
    } else if (!idOrHandle.startsWith("@")) {
      // Check rate limit
      checkRateLimitAndThrow();
      User user = twitter.showUser(idOrHandle);
      if (user == null) {
        throw new Exception("Handle " + idOrHandle + " is not a valid twitter handle.");
      }
      userId = user.getId();
    } else {
      userId = Long.valueOf(idOrHandle);
    }

    long cursor = -1;
    do {
      checkRateLimitAndThrow();
      ResponseList<Status> tweets = twitter.getUserTimeline(userId, paging);
      if (tweets == null || tweets.size() == 0) break;
      for (int i = 0; i < tweets.size(); i++) {
        Status status = tweets.get(i);
        cursor = status.getId() - 1;

        // Ignore retweets
        if (!status.isRetweet()) {
          retval.add(status);
        }
      }
      paging.maxId(cursor);
    } while (true);
    return retval;
  }

  private synchronized void setRateLimitStatus(boolean rateLimitReached, long resetTime) {
    rateLimited = rateLimitReached;
    rateLimitResetTime = resetTime;
  }

  private synchronized boolean isRateLimited() {
    if (rateLimited && System.currentTimeMillis() > rateLimitResetTime) {
      rateLimited = false;
      rateLimitResetTime = -1;
    }
    return rateLimited;
  }

  private void checkRateLimitAndThrow() throws Exception {
    if (isRateLimited()) {
      throw new Exception("The twitter api rate limit has been hit.  " +
          "No more requests will be sent until the rate limit resets at " + LocalTime.from(Instant.ofEpochMilli(rateLimitResetTime)));
    }
  }

  @Override
  public void onRateLimitReached(RateLimitStatusEvent rlStatusEvent) {
    RateLimitStatus rls = rlStatusEvent.getRateLimitStatus();
    setRateLimitStatus(true, ((long) rls.getResetTimeInSeconds()) * 1000L);
    System.err.println("Twitter rate limit reached, stopping all requests for " + rls.getSecondsUntilReset() + " seconds");
  }

  @Override
  public void onRateLimitStatus(RateLimitStatusEvent rlStatusEvent) {
    @SuppressWarnings("unused")
    RateLimitStatus rls = rlStatusEvent.getRateLimitStatus();
  }

}
