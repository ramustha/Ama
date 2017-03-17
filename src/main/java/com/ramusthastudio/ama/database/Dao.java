package com.ramusthastudio.ama.database;

import com.linecorp.bot.model.profile.UserProfileResponse;
import com.ramusthastudio.ama.model.UserLine;
import com.ramusthastudio.ama.model.UserModel;
import java.util.List;
import twitter4j.User;

public interface Dao {
  void setUserModel(User aUser);
  List<UserModel> getAllUserModel();
  UserModel getUserModelById(long aUserId);
  UserModel getUserModelByScreenName(String aScreenName);

  void setUserLine(UserProfileResponse aUser);
  List<UserLine> getAllUserLine();
  UserLine getUserLineById(String aUserId);
}
