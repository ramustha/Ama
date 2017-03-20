package com.ramusthastudio.ama.database;

import com.linecorp.bot.model.profile.UserProfileResponse;
import com.ramusthastudio.ama.model.Actor;
import com.ramusthastudio.ama.model.Evidence;
import com.ramusthastudio.ama.model.Message2;
import com.ramusthastudio.ama.model.UserChat;
import com.ramusthastudio.ama.model.UserLine;
import com.ramusthastudio.ama.model.UserTwitter;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import twitter4j.User;

public class DaoImpl implements Dao {
  private static final Logger LOG = LoggerFactory.getLogger(DaoImpl.class);

  private final static String SQL_SELECT_ALL_USER_TWITTER = "SELECT * FROM user_twitter";
  private final static String SQL_USER_TWITTER_GET_BY_ID = SQL_SELECT_ALL_USER_TWITTER + " WHERE LOWER(id) LIKE LOWER(?) ;";
  private final static String SQL_INSERT_USER_TWITTER = "INSERT INTO user_twitter (id, username, display_name, location, description, profile_image_url,original_profile_image_url, original_profile_image_url_https, is_protected, followers_count, status_text, friends_count, is_verified) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

  private final static String SQL_SELECT_ALL_USER_LINE = "SELECT * FROM user_line";
  private final static String SQL_USER_LINE_GET_BY_ID = SQL_SELECT_ALL_USER_LINE + " WHERE LOWER(id) LIKE LOWER(?) ;";
  private final static String SQL_INSERT_USER_LINE = "INSERT INTO user_line (id, display_name, picture_url, status_message) VALUES (?, ?, ?, ?);";

  private final static String SQL_SELECT_ALL_USER_CHAT = "SELECT * FROM user_chat";
  private final static String SQL_USER_CHAT_GET_BY_ID = SQL_SELECT_ALL_USER_CHAT + " WHERE LOWER(id) LIKE LOWER(?) ;";
  private final static String SQL_INSERT_USER_CHAT = "INSERT INTO user_chat (id, last_chat, last_time) VALUES (?, ?, ?);";
  private final static String SQL_UPDATE_USER_CHAT = "UPDATE user_chat SET last_chat = ?, last_time = ? WHERE LOWER(id) LIKE LOWER(?);";

  private final static String SQL_SELECT_ALL_USER_MESSAGE = "SELECT * FROM user_message";
  private final static String SQL_USER_MESSAGE_GET_BY_LINE_ID = SQL_SELECT_ALL_USER_MESSAGE + " WHERE LOWER(line_id) LIKE LOWER(?) ;";
  private final static String SQL_USER_MESSAGE_GET_BY_TWITTER_ID = SQL_SELECT_ALL_USER_MESSAGE + " WHERE LOWER(twitter_id) LIKE LOWER(?) ;";
  private final static String SQL_INSERT_USER_MESSAGE = "INSERT INTO user_message (line_id, twitter_id, profile_link, posted_time, tweet_link, msg_body, profile_name, friendscount) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

  private final static String SQL_SELECT_ALL_USER_EVIDENCE = "SELECT * FROM user_evidence";
  private final static String SQL_USER_EVIDENCE_GET_BY_MESSAGE_ID = SQL_SELECT_ALL_USER_EVIDENCE + " WHERE LOWER(message_id) LIKE LOWER(?) ;";
  private final static String SQL_INSERT_USER_EVIDENCE = "INSERT INTO user_evidence (message_id, polarity, polarity_term, polarity_term_size) VALUES (?, ?, ?, ?);";

  private final JdbcTemplate mJdbc;

  public DaoImpl(DataSource aDataSource) {
    mJdbc = new JdbcTemplate(aDataSource);
  }

  private final static RowMapper<UserTwitter> SINGLE_USER_TWITTER = (aRs, rowNum) ->
      new UserTwitter(
          aRs.getString("id"),
          aRs.getString("username"),
          aRs.getString("display_name"),
          aRs.getString("location"),
          aRs.getString("description"),
          aRs.getString("profile_image_url"),
          aRs.getString("original_profile_image_url"),
          aRs.getString("original_profile_image_url_https"),
          aRs.getBoolean("is_protected"),
          aRs.getInt("followers_count"),
          aRs.getString("status_text"),
          aRs.getInt("friends_count"),
          aRs.getBoolean("is_verified"));

  private final static RowMapper<UserLine> SINGLE_USER_LINE = (aRs, rowNum) ->
      new UserLine(
          aRs.getString("id"),
          aRs.getString("display_name"),
          aRs.getString("picture_url"),
          aRs.getString("status_message"));

  private final static RowMapper<UserChat> SINGLE_USER_CHAT = (aRs, rowNum) ->
      new UserChat(
          aRs.getString("id"),
          aRs.getString("last_chat"),
          aRs.getTimestamp("last_time").getTime());

  private final static RowMapper<Message2> SINGLE_USER_MESSAGE = (aRs, rowNum) ->
      new Message2(
          aRs.getString("line_id"),
          aRs.getString("twitter_id"),
          aRs.getString("profile_link"),
          aRs.getTimestamp("posted_time").getTime(),
          aRs.getString("tweet_link"),
          aRs.getString("msg_body"),
          aRs.getString("profile_name"),
          aRs.getInt("friends_count"));

  private final static RowMapper<Evidence> SINGLE_USER_EVIDENCE = (aRs, rowNum) ->
      new Evidence(
          aRs.getString("message_id"),
          aRs.getString("polarity"),
          aRs.getString("polarity_term"),
          aRs.getInt("polarity_term_size"));

  private final static ResultSetExtractor<List<UserTwitter>> MULTIPLE_USER_TWITTER = aRs -> {
    List<UserTwitter> list = new ArrayList<>();
    while (aRs.next()) {
      list.add(new UserTwitter(
          aRs.getString("id"),
          aRs.getString("username"),
          aRs.getString("username"),
          aRs.getString("location"),
          aRs.getString("description"),
          aRs.getString("profile_image_url"),
          aRs.getString("original_profile_image_url"),
          aRs.getString("original_profile_image_url_https"),
          aRs.getBoolean("is_protected"),
          aRs.getInt("followers_count"),
          aRs.getString("status_text"),
          aRs.getInt("friends_count"),
          aRs.getBoolean("is_verified")
      ));
    }
    return list;
  };

  private final static ResultSetExtractor<List<UserLine>> MULTIPLE_USER_LINE = aRs -> {
    List<UserLine> list = new ArrayList<>();
    while (aRs.next()) {
      list.add(new UserLine(
          aRs.getString("id"),
          aRs.getString("display_name"),
          aRs.getString("picture_url"),
          aRs.getString("status_message")
      ));
    }
    return list;
  };

  private final static ResultSetExtractor<List<UserChat>> MULTIPLE_USER_CHAT = aRs -> {
    List<UserChat> list = new ArrayList<>();
    while (aRs.next()) {
      list.add(new UserChat(
          aRs.getString("id"),
          aRs.getString("last_chat"),
          aRs.getTimestamp("last_time").getTime()
      ));
    }
    return list;
  };

  private final static ResultSetExtractor<List<Message2>> MULTIPLE_USER_MESSAGE = aRs -> {
    List<Message2> list = new ArrayList<>();
    while (aRs.next()) {
      list.add(new Message2(
          aRs.getString("line_id"),
          aRs.getString("twitter_id"),
          aRs.getString("profile_link"),
          aRs.getTimestamp("posted_time").getTime(),
          aRs.getString("tweet_link"),
          aRs.getString("msg_body"),
          aRs.getString("profile_name"),
          aRs.getInt("friends_count")));
    }
    return list;
  };

  private final static ResultSetExtractor<List<Evidence>> MULTIPLE_USER_EVIDENCE = aRs -> {
    List<Evidence> list = new ArrayList<>();
    while (aRs.next()) {
      list.add(new Evidence(
          aRs.getString("message_id"),
          aRs.getString("polarity"),
          aRs.getString("polarity_term"),
          aRs.getInt("polarity_term_size")));
    }
    return list;
  };

  @Override public void setUserTwitter(User aUser) {
    mJdbc.update(SQL_INSERT_USER_TWITTER,
        aUser.getScreenName(),
        aUser.getScreenName(),
        aUser.getName(),
        aUser.getLocation(),
        aUser.getDescription(),
        aUser.getProfileImageURL(),
        aUser.getOriginalProfileImageURL(),
        aUser.getOriginalProfileImageURLHttps(),
        aUser.isProtected(),
        aUser.getFollowersCount(),
        aUser.getStatus().getText(),
        aUser.getFriendsCount(),
        aUser.isVerified());
  }

  @Override public void setUserLine(UserProfileResponse aUser) {
    mJdbc.update(SQL_INSERT_USER_LINE,
        aUser.getUserId(),
        aUser.getDisplayName(),
        aUser.getPictureUrl(),
        aUser.getStatusMessage());
  }

  @Override public void setUserChat(UserChat aUser) {
    mJdbc.update(SQL_INSERT_USER_CHAT,
        aUser.getUserId(),
        aUser.getLastChat(),
        new Timestamp(aUser.getLastTime()));
  }

  @Override public void setUserMessage(Message2 aMessage2) {
    LOG.info("setUserMessage "+aMessage2);
    if (aMessage2.getActor() != null) {
      Actor actor = aMessage2.getActor();
      mJdbc.update(SQL_INSERT_USER_MESSAGE,
          aMessage2.getLineId(),
          aMessage2.getTwitterId(),
          actor.getLink(),
          Date.valueOf(aMessage2.getPostedTime()),
          aMessage2.getLink(),
          aMessage2.getBody(),
          actor.getDisplayName(),
          actor.getFriendsCount()
      );
    }
  }

  @Override public void setUserEvidence(Evidence aEvidence) {
    LOG.info("setUserEvidence "+aEvidence);
    mJdbc.update(SQL_INSERT_USER_EVIDENCE,
        aEvidence.getId(),
        aEvidence.getPolarity(),
        aEvidence.getSentimentTerm(),
        aEvidence.getSize()
    );
  }

  @Override public void updateUserChat(UserChat aUser) {
    mJdbc.update(SQL_UPDATE_USER_CHAT,
        aUser.getLastChat(),
        new Timestamp(aUser.getLastTime()),
        aUser.getUserId()
    );
  }

  @Override public List<UserTwitter> getAllUserTwitter() {
    return mJdbc.query(SQL_SELECT_ALL_USER_TWITTER, MULTIPLE_USER_TWITTER);
  }

  @Override public List<UserLine> getAllUserLine() {
    return mJdbc.query(SQL_SELECT_ALL_USER_LINE, MULTIPLE_USER_LINE);
  }

  @Override public List<UserChat> getAllUserChat() {
    return mJdbc.query(SQL_SELECT_ALL_USER_CHAT, MULTIPLE_USER_CHAT);
  }

  @Override public List<Message2> getAllUserMessage() {
    return mJdbc.query(SQL_SELECT_ALL_USER_MESSAGE, MULTIPLE_USER_MESSAGE);
  }

  @Override public List<Evidence> getAllUserEvidence() {
    return mJdbc.query(SQL_SELECT_ALL_USER_EVIDENCE, MULTIPLE_USER_EVIDENCE);
  }

  @Override public UserTwitter getUserTwitterById(String aUserId) {
    try {
      return mJdbc.queryForObject(SQL_USER_TWITTER_GET_BY_ID, new Object[] {"%" + aUserId + "%"}, SINGLE_USER_TWITTER);
    } catch (Exception e) {
      LOG.error("Error when trying get UserTwitter cause : " + e.getMessage());
      return null;
    }
  }

  @Override public Message2 getUserMessageByLineId(String aLineId) {
    try {
      return mJdbc.queryForObject(SQL_USER_MESSAGE_GET_BY_LINE_ID, new Object[] {"%" + aLineId + "%"}, SINGLE_USER_MESSAGE);
    } catch (Exception e) {
      LOG.error("Error when trying get Message2 cause : " + e.getMessage());
      return null;
    }
  }

  @Override public Message2 getUserMessageByTwitterId(String aTwitterId) {
    try {
      return mJdbc.queryForObject(SQL_USER_MESSAGE_GET_BY_TWITTER_ID, new Object[] {"%" + aTwitterId + "%"}, SINGLE_USER_MESSAGE);
    } catch (Exception e) {
      LOG.error("Error when trying get Message2 cause : " + e.getMessage());
      return null;
    }
  }

  @Override public Evidence getUserEvidenceByMessageId(String aMessageId) {
    try {
      return mJdbc.queryForObject(SQL_USER_EVIDENCE_GET_BY_MESSAGE_ID, new Object[] {"%" + aMessageId + "%"}, SINGLE_USER_EVIDENCE);
    } catch (Exception e) {
      LOG.error("Error when trying get Evidence cause : " + e.getMessage());
      return null;
    }
  }

  @Override public UserLine getUserLineById(String aUserId) {
    try {
      return mJdbc.queryForObject(SQL_USER_LINE_GET_BY_ID, new Object[] {"%" + aUserId + "%"}, SINGLE_USER_LINE);
    } catch (Exception e) {
      LOG.error("Error when trying get UserLine cause : " + e.getMessage());
      return null;
    }
  }

  @Override public UserChat getUserChatById(String aUserId) {
    try {
      return mJdbc.queryForObject(SQL_USER_CHAT_GET_BY_ID, new Object[] {"%" + aUserId + "%"}, SINGLE_USER_CHAT);
    } catch (Exception e) {
      LOG.error("Error when trying get UserChat cause : " + e.getMessage());
      return null;
    }
  }
}
