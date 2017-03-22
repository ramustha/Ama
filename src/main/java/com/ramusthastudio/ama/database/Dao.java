package com.ramusthastudio.ama.database;

import com.linecorp.bot.model.profile.UserProfileResponse;
import com.ramusthastudio.ama.model.Evidence;
import com.ramusthastudio.ama.model.Message2;
import com.ramusthastudio.ama.model.UserChat;
import com.ramusthastudio.ama.model.UserConsumption;
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

  List<UserChat> getAllUserChat();
  void setUserChat(UserChat aUser);
  void updateUserChat(UserChat aUser);
  UserChat getUserChatById(String aUserId);

  List<Message2> getAllUserMessage();
  void setUserMessage(Message2 aMessage2);
  List<Message2> getUserMessageByLineId(String aLineId);
  List<Message2> getUserMessageByTwitterId(String aTwitterId);

  List<Evidence> getAllUserEvidence();
  void setUserEvidence(Evidence aUser);
  List<Evidence> getUserEvidenceByMessageId(String aTwitterId);

  List<UserConsumption> getAllUserConsumption();
  void setUserConsumption(UserConsumption aUserConsumption);
  List<UserConsumption> getUserConsumptionByTwitterId(String aTwitterId);
}
