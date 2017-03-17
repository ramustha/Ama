package com.ramusthastudio.ama.task;

import com.ramusthastudio.ama.model.UserLine;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
  private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

  @Autowired
  @Qualifier("line.bot.channelSecret")
  String fChannelSecret;

  @Autowired
  @Qualifier("line.bot.channelToken")
  String fChannelAccessToken;

  @Autowired
  UserLine fUserLine;

  @Scheduled(fixedRate = 5000)
  public void reportCurrentTime() {
    if (fUserLine != null) {
      log.info("The time is now {} : {}", dateFormat.format(new Date()), fUserLine.getDisplayName());
    }
  }
}
