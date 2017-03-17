package com.ramusthastudio.ama.task;

import com.ramusthastudio.ama.database.Dao;
import com.ramusthastudio.ama.model.UserChat;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
      LocalDateTime now = LocalDateTime.now();
      LOG.info("The time is now {}", dateFormat.format(now));
      List<UserChat> userChat = mDao.getAllUserChat();
      if (userChat != null && userChat.size() > 0) {
        for (UserChat chat : userChat) {
          LocalDateTime lastTimeChat = LocalDateTime.from(Instant.ofEpochMilli(chat.getLastTime()));
          LocalDateTime timeLimit = lastTimeChat.plusMinutes(2);
          if (timeLimit.isAfter(now)) {
            LOG.info("Start push message");
            // try {
            //   pushMessage(fChannelAccessToken, chat.getUserId(), "Kok kamu aja ? kok gak ngobrol sama aku lagi ?");
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
