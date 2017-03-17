package com.ramusthastudio.ama.database;

import com.ramusthastudio.ama.model.UserModel;
import java.util.List;
import twitter4j.User;

public interface Dao {
  void setUser(User aUser);
  List<UserModel> getAllUserModel();
  UserModel getUserModelById(long aUserId);
  UserModel getUserModelByScreenName(String aScreenName);
}
