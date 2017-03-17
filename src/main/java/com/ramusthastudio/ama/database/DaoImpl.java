package com.ramusthastudio.ama.database;

import com.linecorp.bot.model.profile.UserProfileResponse;
import com.ramusthastudio.ama.model.UserLine;
import com.ramusthastudio.ama.model.UserTwitter;
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

  private final JdbcTemplate mJdbc;

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

  public DaoImpl(DataSource aDataSource) {
    mJdbc = new JdbcTemplate(aDataSource);
  }

  @Override public void setUserTwitter(User aUser) {
    mJdbc.update(SQL_INSERT_USER_TWITTER,
        aUser.getScreenName(),
        aUser.getScreenName(),
        aUser.getName(),
        aUser.getLocation(),
        aUser.getDescription(),
        aUser.getProfileImageURL(),
        aUser.getOriginalProfileImageURL(),
        aUser.getProfileImageURLHttps(),
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

  @Override public List<UserTwitter> getAllUserTwitter() {
    return mJdbc.query(SQL_SELECT_ALL_USER_TWITTER, MULTIPLE_USER_TWITTER);
  }

  @Override public List<UserLine> getAllUserLine() {
    return mJdbc.query(SQL_SELECT_ALL_USER_LINE, MULTIPLE_USER_LINE);
  }

  @Override public UserTwitter getUserTwitterById(String aUserId) {
    try {
      return mJdbc.queryForObject(SQL_USER_TWITTER_GET_BY_ID, new Object[] {"%" + aUserId + "%"}, SINGLE_USER_TWITTER);
    } catch (Exception e) {
      LOG.error("Error when trying get UserModel cause : " + e.getMessage());
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
}
