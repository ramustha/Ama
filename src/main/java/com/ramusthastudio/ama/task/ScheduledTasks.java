package com.ramusthastudio.ama.task;

import com.ramusthastudio.ama.database.Dao;
import com.ramusthastudio.ama.model.UserChat;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.ramusthastudio.ama.util.BotHelper.pushMessage;
import static org.apache.tomcat.jni.Time.now;

@Component
public class ScheduledTasks {
  private static final Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");

  @Autowired
  @Qualifier("line.bot.channelSecret")
  String fChannelSecret;

  @Autowired
  @Qualifier("line.bot.channelToken")
  String fChannelAccessToken;

  @Autowired
  Dao mDao;

  @Scheduled(fixedRate = 5000)
  public void reportCurrentTime() {
    try {
      LOG.info("The time is now {}", dateFormat.format(new Date()));
      List<UserChat> userChat = mDao.getAllUserChat();
      if (userChat != null && userChat.size() > 0) {
        for (UserChat chat : userChat) {
          Timestamp lastTimeChat = new Timestamp(chat.getLastTime());
          LocalDateTime timeLimit = lastTimeChat.toLocalDateTime().plusMinutes(2);
          if (timeLimit.isBefore(LocalDateTime.now())) {
            LOG.info("Start push message");
            mDao.updateUserChat(new UserChat(chat.getUserId(), "Start push message", now()));
            // try {
            //   String text = "Kok kamu aja ? kok gak ngobrol sama aku lagi ?";
            //   pushMessage(fChannelAccessToken, chat.getUserId(), text);
            // } catch (IOException aE) {
            //   LOG.error("Start push message error message : " + aE.getMessage());
            // }
          }
        }
      }
    } catch (Exception aE) {
      LOG.error("ScheduledTasks error message : " + aE.getMessage());
    }
  }
}
