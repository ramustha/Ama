package com.ramusthastudio.ama.database;

import com.linecorp.bot.model.profile.UserProfileResponse;
import com.ramusthastudio.ama.model.UserLine;
import com.ramusthastudio.ama.model.UserTwitter;
import java.util.List;
import twitter4j.User;

public interface Dao {
  List<UserTwitter> getAllUserTwitter();
  void setUserTwitter(User aUser);
  UserTwitter getUserTwitterById(String aUserId);

  List<UserLine> getAllUserLine();
  void setUserLine(UserProfileResponse aUser);
  UserLine getUserLineById(String aUserId);
}
