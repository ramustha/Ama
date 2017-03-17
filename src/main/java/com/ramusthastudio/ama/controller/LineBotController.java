package com.ramusthastudio.ama.controller;

import com.google.gson.Gson;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.ramusthastudio.ama.database.Dao;
import com.ramusthastudio.ama.model.Events;
import com.ramusthastudio.ama.model.Message;
import com.ramusthastudio.ama.model.Payload;
import com.ramusthastudio.ama.model.Postback;
import com.ramusthastudio.ama.model.Source;
import com.ramusthastudio.ama.model.UserLine;
import com.ramusthastudio.ama.model.UserModel;
import com.ramusthastudio.ama.util.StickerHelper;
import com.ramusthastudio.ama.util.Twitter4JHelper;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import twitter4j.User;

import static com.ramusthastudio.ama.util.BotHelper.FOLLOW;
import static com.ramusthastudio.ama.util.BotHelper.MESSAGE;
import static com.ramusthastudio.ama.util.BotHelper.MESSAGE_TEXT;
import static com.ramusthastudio.ama.util.BotHelper.POSTBACK;
import static com.ramusthastudio.ama.util.BotHelper.TWITTER;
import static com.ramusthastudio.ama.util.BotHelper.TWITTER_FALSE;
import static com.ramusthastudio.ama.util.BotHelper.TWITTER_NO;
import static com.ramusthastudio.ama.util.BotHelper.TWITTER_TRUE;
import static com.ramusthastudio.ama.util.BotHelper.TWITTER_YES;
import static com.ramusthastudio.ama.util.BotHelper.UNFOLLOW;
import static com.ramusthastudio.ama.util.BotHelper.confirmTwitterMessage;
import static com.ramusthastudio.ama.util.BotHelper.getUserProfile;
import static com.ramusthastudio.ama.util.BotHelper.greetingMessage;
import static com.ramusthastudio.ama.util.BotHelper.instructionTweetsMessage;
import static com.ramusthastudio.ama.util.BotHelper.profileUserMessage;
import static com.ramusthastudio.ama.util.BotHelper.replayMessage;
import static com.ramusthastudio.ama.util.BotHelper.stickerMessage;
import static com.ramusthastudio.ama.util.BotHelper.unfollowMessage;
import static com.ramusthastudio.ama.util.StickerHelper.JAMES_STICKER_SHOCK;

@RestController
@RequestMapping(value = "/linebot")
public class LineBotController {
  private static final Logger LOG = LoggerFactory.getLogger(LineBotController.class);

  @Autowired
  @Qualifier("line.bot.channelSecret")
  String fChannelSecret;

  @Autowired
  @Qualifier("line.bot.channelToken")
  String fChannelAccessToken;

  @Autowired
  Dao mDao;

  private Twitter4JHelper twitterHelper;

  @RequestMapping(value = "/callback", method = RequestMethod.POST)
  public ResponseEntity<String> callback(
      @RequestHeader("X-Line-Signature") String aXLineSignature,
      @RequestBody String aPayload) {

    LOG.info("XLineSignature: {} ", aXLineSignature);
    LOG.info("Payload: {} ", aPayload);

    LOG.info("The Signature is: {} ", (aXLineSignature != null && aXLineSignature.length() > 0) ? aXLineSignature : "N/A");
    final boolean valid = new LineSignatureValidator(fChannelSecret.getBytes()).validateSignature(aPayload.getBytes(), aXLineSignature);
    LOG.info("The Signature is: {} ", valid ? "valid" : "tidak valid");

    if (twitterHelper == null) {
      twitterHelper = new Twitter4JHelper();
    }

    if (aPayload != null && aPayload.length() > 0) {
      Gson gson = new Gson();
      Payload payload = gson.fromJson(aPayload, Payload.class);

      Events event = payload.events()[0];

      String eventType = event.type();
      String replayToken = event.replyToken();
      Source source = event.source();
      long timestamp = event.timestamp();
      Message message = event.message();
      Postback postback = event.postback();

      String userId = source.userId();

      UserLine userLine = null;
      UserModel userModel = null;

      try {
        LOG.info("Start find UserProfileResponse on database...");
        UserProfileResponse profile = getUserProfile(fChannelAccessToken, userId);
        userLine = mDao.getUserLineById(profile.getUserId());
        if (userLine == null) {
          LOG.info("Start save user line to database...");
          mDao.setUserLine(profile);
        }
        LOG.info("End find UserProfileResponse on database...");
      } catch (IOException ignored) {}

      try {
        switch (eventType) {
          case UNFOLLOW:
            unfollowMessage(fChannelAccessToken, userId);
            break;
          case FOLLOW:
            LOG.info("Greeting Message");
            greetingMessage(fChannelAccessToken, userId);
            confirmTwitterMessage(fChannelAccessToken, userId);
            break;
          case MESSAGE:
            if (message.type().equals(MESSAGE_TEXT)) {
              String text = message.text();
              if (text.toLowerCase().startsWith(TWITTER)) {
                String screenName = text.substring(TWITTER.length(), text.length());

                if (screenName.length() > 3) {
                  LOG.info("Start find user on database...");
                  userModel = mDao.getUserModelByScreenName(screenName);
                  LOG.info("end find user on database...");

                  if (userModel != null) {
                    profileUserMessage(fChannelAccessToken, userId, userModel);
                  } else {
                    LOG.info("Start find user on twitter server...");
                    try {
                      User twitterUser = twitterHelper.checkUsers(screenName);
                      profileUserMessage(fChannelAccessToken, userId, twitterUser);
                      LOG.info("Start adding user...");
                      mDao.setUserModel(twitterUser);
                      LOG.info("End adding user...");
                      confirmTwitterMessage(fChannelAccessToken, userId, "Bener ini twitter nya ?", TWITTER_TRUE, TWITTER_FALSE);

                    } catch (Exception aE) {
                      LOG.error("Getting twitter info error message : " + aE.getMessage());
                    }
                    LOG.info("End find user on twitter server...");
                  }
                } else {
                  replayMessage(fChannelAccessToken, replayToken, "Yakin id nya udah bener ? coba cek lagi id nya...");
                }

              } else {
                replayMessage(fChannelAccessToken, replayToken, message.text());
              }
            }
            break;
          case POSTBACK:
            String pd = postback.data();

            switch (pd) {
              case TWITTER_YES:
                replayMessage(fChannelAccessToken, replayToken, TWITTER_YES + "tw: " + userModel.getName() +" line: "+userLine.getDisplayName());
                instructionTweetsMessage(fChannelAccessToken, userId);
                break;
              case TWITTER_NO:
                stickerMessage(fChannelAccessToken, userId, new StickerHelper.StickerMsg(JAMES_STICKER_SHOCK));
                replayMessage(fChannelAccessToken, replayToken, "Hari gini gak punya twitter ?");
                break;
              case TWITTER_TRUE:
                replayMessage(fChannelAccessToken, replayToken, TWITTER_YES + "tw: " + userModel.getName() +" line: "+userLine.getDisplayName());
                stickerMessage(fChannelAccessToken, userId, new StickerHelper.StickerMsg(JAMES_STICKER_SHOCK));
                break;
              case TWITTER_FALSE:
                replayMessage(fChannelAccessToken, replayToken, "Salah ? trus ini siapa ?");
                break;
            }
            break;
        }
      } catch (IOException aE) { LOG.error("Message {}", aE.getMessage()); }
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
