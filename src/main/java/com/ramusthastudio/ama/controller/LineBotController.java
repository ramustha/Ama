package com.ramusthastudio.ama.controller;

import com.google.gson.Gson;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.ramusthastudio.ama.model.Events;
import com.ramusthastudio.ama.model.Message;
import com.ramusthastudio.ama.model.Payload;
import com.ramusthastudio.ama.model.Postback;
import com.ramusthastudio.ama.model.Source;
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
import retrofit2.Response;

import static com.ramusthastudio.ama.util.BotHelper.FOLLOW;
import static com.ramusthastudio.ama.util.BotHelper.MESSAGE;
import static com.ramusthastudio.ama.util.BotHelper.MESSAGE_TEXT;
import static com.ramusthastudio.ama.util.BotHelper.POSTBACK;
import static com.ramusthastudio.ama.util.BotHelper.TWITTER;
import static com.ramusthastudio.ama.util.BotHelper.TWITTER_NO;
import static com.ramusthastudio.ama.util.BotHelper.TWITTER_YES;
import static com.ramusthastudio.ama.util.BotHelper.confirmTwitterMessage;
import static com.ramusthastudio.ama.util.BotHelper.getUserProfile;
import static com.ramusthastudio.ama.util.BotHelper.greetingMessage;
import static com.ramusthastudio.ama.util.BotHelper.replayMessage;

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

  @RequestMapping(value = "/callback", method = RequestMethod.POST)
  public ResponseEntity<String> callback(
      @RequestHeader("X-Line-Signature") String aXLineSignature,
      @RequestBody String aPayload) {

    LOG.info("XLineSignature: {} ", aXLineSignature);
    LOG.info("Payload: {} ", aPayload);

    LOG.info("The Signature is: {} ", (aXLineSignature != null && aXLineSignature.length() > 0) ? aXLineSignature : "N/A");
    final boolean valid = new LineSignatureValidator(fChannelSecret.getBytes()).validateSignature(aPayload.getBytes(), aXLineSignature);
    LOG.info("The Signature is: {} ", valid ? "valid" : "tidak valid");

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

      LOG.info("Getting user profile");
      try {
        Response<UserProfileResponse> profileResp = getUserProfile(fChannelAccessToken, userId);
        UserProfileResponse profile = profileResp.body();
        LOG.info("profile {} message {}", profile, profileResp.message());
      } catch (IOException aE) {
        LOG.error("Message {}", aE.getMessage());
      }

      try {
        switch (eventType) {
          case FOLLOW:
            LOG.info("Greeting Message");
            greetingMessage(fChannelAccessToken, userId);
            confirmTwitterMessage(fChannelAccessToken, userId);
            break;
          case MESSAGE:
            if (message.type().equals(MESSAGE_TEXT)) {
              String text = message.text();
              if (text.toLowerCase().startsWith(TWITTER)) {
                String id = text.substring(TWITTER.length(), text.length());
                replayMessage(fChannelAccessToken, replayToken, "twitter:" + id + "\n" + "Tunggu sebentar yah...");
              } else {
                replayMessage(fChannelAccessToken, replayToken, message.text());
              }
            }
            break;
          case POSTBACK:
            String pd = postback.data();
            if (pd.equalsIgnoreCase(TWITTER_YES)) {
              replayMessage(fChannelAccessToken, replayToken, TWITTER_YES);
            } else if (pd.equalsIgnoreCase(TWITTER_NO)) {
              replayMessage(fChannelAccessToken, replayToken, TWITTER_NO);
            }
            break;
        }
      } catch (IOException aE) {
        LOG.error("Message {}", aE.getMessage());
      }

    }

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
