package com.ramusthastudio.ama.database;

import com.ramusthastudio.ama.model.UserModel;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import twitter4j.User;

public class DaoImpl implements Dao {
  private final static String SQL_SELECT_ALL = "SELECT * FROM \"user\"";
  private final static String SQL_GET_BY_ID = SQL_SELECT_ALL + " WHERE id = ? ;";
  private final static String SQL_GET_BY_SCREEN_NAME = SQL_SELECT_ALL + " WHERE LOWER(screen_name) LIKE LOWER(?) ;";
  private final static String SQL_INSERT_USER = "INSERT INTO \"user\" (id, \"name\", screen_name, location, description, profile_image_url,original_profile_image_url, original_profile_image_url_https, is_protected, followers_count, status_text, friends_count, is_verified) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

  private final JdbcTemplate mJdbc;

  private final static RowMapper<UserModel> SINGLE_USER_TWITTER = (aRs, rowNum) ->
      new UserModel(
          aRs.getInt("id"),
          aRs.getString("name"),
          aRs.getString("screen_name"),
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

  private final static ResultSetExtractor<List<UserModel>> MULTIPLE_USER_TWITTER = aRs -> {
    List<UserModel> list = new ArrayList<>();
    while (aRs.next()) {
      list.add(new UserModel(
          aRs.getInt("id"),
          aRs.getString("name"),
          aRs.getString("screen_name"),
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

  public DaoImpl(DataSource aDataSource) {
    mJdbc = new JdbcTemplate(aDataSource);
  }

  @Override public void setUser(User aUser) {
    mJdbc.update(SQL_INSERT_USER,
        aUser.getId(),
        aUser.getName(),
        aUser.getScreenName(),
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

  @Override public List<UserModel> getAllUserModel() {
    return mJdbc.query(SQL_SELECT_ALL, MULTIPLE_USER_TWITTER);
  }

  @Override public UserModel getUserModelById(long aUserId) {
    try {
      return mJdbc.queryForObject(SQL_GET_BY_ID, new Object[] {"%" + aUserId + "%"}, SINGLE_USER_TWITTER);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  @Override public UserModel getUserModelByScreenName(String aScreenName) {
    try {
      return mJdbc.queryForObject(SQL_GET_BY_SCREEN_NAME, new Object[] {"%" + aScreenName + "%"}, SINGLE_USER_TWITTER);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }
}
