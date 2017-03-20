package com.ramusthastudio.ama.controller;

import com.google.gson.Gson;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.ramusthastudio.ama.database.Dao;
import com.ramusthastudio.ama.model.ApiTweets;
import com.ramusthastudio.ama.model.Content;
import com.ramusthastudio.ama.model.Events;
import com.ramusthastudio.ama.model.Evidence;
import com.ramusthastudio.ama.model.Message;
import com.ramusthastudio.ama.model.Message2;
import com.ramusthastudio.ama.model.Payload;
import com.ramusthastudio.ama.model.Postback;
import com.ramusthastudio.ama.model.Sentiment;
import com.ramusthastudio.ama.model.SentimentTweetService;
import com.ramusthastudio.ama.model.Source;
import com.ramusthastudio.ama.model.Tweet;
import com.ramusthastudio.ama.model.UserChat;
import com.ramusthastudio.ama.model.UserLine;
import com.ramusthastudio.ama.model.UserTwitter;
import com.ramusthastudio.ama.util.StickerHelper;
import com.ramusthastudio.ama.util.Twitter4JHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
import retrofit2.Call;
import retrofit2.Response;
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
import static com.ramusthastudio.ama.util.StickerHelper.JAMES_STICKER_CHEERS;
import static com.ramusthastudio.ama.util.StickerHelper.JAMES_STICKER_SHOCK;

@RestController
@RequestMapping(value = "/linebot")
public class LineBotController {
  private static final Logger LOG = LoggerFactory.getLogger(LineBotController.class);
  private final static int MAX_TWEETS = 500;
  private final static int TWEETS_STEP = 250;
  private Twitter4JHelper fTwitterHelper;
  private final List<Message2> collectMessage = new ArrayList<>();
  private final List<Evidence> collectEvidence = new ArrayList<>();

  @Autowired
  @Qualifier("line.bot.channelSecret")
  String fChannelSecret;
  @Autowired
  @Qualifier("line.bot.channelToken")
  String fChannelAccessToken;
  @Autowired
  Dao fDao;
  @Autowired
  SentimentTweetService fSentimentTweetService;

  @RequestMapping(value = "/callback", method = RequestMethod.POST)
  public ResponseEntity<String> callback(
      @RequestHeader("X-Line-Signature") String aXLineSignature,
      @RequestBody String aPayload) {

    LOG.info("XLineSignature: {} ", aXLineSignature);
    LOG.info("Payload: {} ", aPayload);

    LOG.info("The Signature is: {} ", (aXLineSignature != null && aXLineSignature.length() > 0) ? aXLineSignature : "N/A");
    final boolean valid = new LineSignatureValidator(fChannelSecret.getBytes()).validateSignature(aPayload.getBytes(), aXLineSignature);
    LOG.info("The Signature is: {} ", valid ? "valid" : "tidak valid");

    if (fTwitterHelper == null) {
      fTwitterHelper = new Twitter4JHelper();
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

      try {
        LOG.info("Start find UserProfileResponse on database...");
        UserProfileResponse profile = getUserProfile(fChannelAccessToken, userId);
        UserLine mUserLine = fDao.getUserLineById(profile.getUserId());
        if (mUserLine == null) {
          LOG.info("Start save user line to database...");
          fDao.setUserLine(profile);
        }
        LOG.info("End find UserProfileResponse on database..." + mUserLine);
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

              UserChat userChat = fDao.getUserChatById(userId);
              if (userChat != null) {
                LOG.info("Start updating chat history...");
                fDao.updateUserChat(new UserChat(userId, text, timestamp));
              } else {
                LOG.info("Start saving chat history...");
                fDao.setUserChat(new UserChat(userId, text, timestamp));
              }

              if (text.toLowerCase().startsWith(TWITTER)) {
                String screenName = text.substring(TWITTER.length(), text.length());

                if (screenName.length() > 3) {
                  LOG.info("Start find user on database..." + screenName);
                  UserTwitter userTwitter = fDao.getUserTwitterById(screenName);
                  LOG.info("end find user on database..." + userTwitter);

                  if (userTwitter != null) {
                    LOG.info("Display from database...");
                    profileUserMessage(fChannelAccessToken, userId, userTwitter);
                  } else {
                    try {
                      User twitterUser = fTwitterHelper.checkUsers(screenName);
                      LOG.info("Display from twitter server...");
                      profileUserMessage(fChannelAccessToken, userId, twitterUser);
                      LOG.info("Start adding user...");
                      fDao.setUserTwitter(twitterUser);
                      LOG.info("End adding user...");
                    } catch (Exception aE) {
                      LOG.error("Getting twitter info error message : " + aE.getMessage());
                    }
                  }

                  confirmTwitterMessage(fChannelAccessToken, userId, "Bener ini twitter nya ?", TWITTER_TRUE + screenName, TWITTER_FALSE);
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

            if (pd.startsWith(TWITTER_YES)) {
              replayMessage(fChannelAccessToken, replayToken, TWITTER_YES);
              instructionTweetsMessage(fChannelAccessToken, userId);
            } else if (pd.startsWith(TWITTER_NO)) {
              stickerMessage(fChannelAccessToken, userId, new StickerHelper.StickerMsg(JAMES_STICKER_SHOCK));
              replayMessage(fChannelAccessToken, replayToken, "Hari gini gak punya twitter ?");
            } else if (pd.startsWith(TWITTER_TRUE)) {
              String screenName = pd.substring(TWITTER_TRUE.length(), pd.length());
              UserTwitter userTwitter = fDao.getUserTwitterById(screenName);

              List<Message2> message2 = fDao.getUserMessageByTwitterId(userTwitter.getId());
              List<Evidence> evidence = fDao.getUserEvidenceByMessageId(userTwitter.getId());
              if (message2.size() > 0 && evidence.size() > 0) {
                LOG.info("Start find sentiment from database..." + userTwitter);
                pushSentiment(replayToken, userId, message2, evidence);
                LOG.info("End find sentiment from database..." + userTwitter);
              } else {
                sentimentService(replayToken, userId, userTwitter);
              }

            } else if (pd.startsWith(TWITTER_FALSE)) {
              replayMessage(fChannelAccessToken, replayToken, "Salah ? trus ini siapa ?");
            }
            break;
        }
      } catch (IOException aE) { LOG.error("Message {}", aE.getMessage()); }
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  private void sentimentService(String aReplayToken, String aUserId, UserTwitter aUserTwitter) throws IOException {
    LOG.info("Start sentiment service..." + aUserTwitter);
    Call<ApiTweets> tweets = fSentimentTweetService.apiTweets(aUserTwitter.getUsername(), MAX_TWEETS);
    Response<ApiTweets> exec = tweets.execute();
    ApiTweets apiTweets = exec.body();
    // Search resultSearch = apiTweets.getSearch();
    List<Tweet> resultTweets = apiTweets.getTweets();
    // Related resultRelated = apiTweets.getRelated();
    polarityProcess(aUserId, aUserTwitter.getId(), resultTweets);
    for (Message2 message2 : collectMessage) { fDao.setUserMessage(message2); }
    for (Evidence evidence : collectEvidence) { fDao.setUserEvidence(evidence); }
    pushSentiment(aReplayToken, aUserId, collectMessage, collectEvidence);
    LOG.info("End sentiment service..." + aUserTwitter);
  }

  private void pushSentiment(String aReplayToken, String aUserId, List<Message2> aMessage2, List<Evidence> aEvidence) throws IOException {
    StringBuilder b = new StringBuilder("Kata orang kamu tuh ");

    if (aMessage2.size() > 3) {
      Random rand1 = new Random();
      Random rand2 = new Random();
      Random rand3 = new Random();
      rand1.nextInt(aMessage2.size());
      rand2.nextInt(aMessage2.size());
      rand3.nextInt(aMessage2.size());
      b.append(aEvidence.get(rand1.nextInt(aMessage2.size())).getSentimentTerm());
      b.append(", ").append(aEvidence.get(rand2.nextInt(aMessage2.size())).getSentimentTerm());
      b.append(", ").append(aEvidence.get(rand3.nextInt(aMessage2.size())).getSentimentTerm());
    } else if (aMessage2.size() > 2) {
      Random rand1 = new Random();
      Random rand2 = new Random();
      rand1.nextInt(aMessage2.size());
      rand2.nextInt(aMessage2.size());
      b.append(aEvidence.get(rand1.nextInt(aMessage2.size())).getSentimentTerm());
      b.append(", ").append(aEvidence.get(rand2.nextInt(aMessage2.size())).getSentimentTerm());
    } else if (aMessage2.size() > 1) {
      Random rand1 = new Random();
      rand1.nextInt(aMessage2.size());
      b.append(aEvidence.get(rand1.nextInt(aMessage2.size())).getSentimentTerm());
    }

    replayMessage(fChannelAccessToken, aReplayToken, b.toString());
    stickerMessage(fChannelAccessToken, aUserId, new StickerHelper.StickerMsg(JAMES_STICKER_CHEERS));
  }

  private void polarityProcess(String aLineId, String aTwitterId, List<Tweet> resultTweets) {
    int posittiveCount = 0;
    int negativeCount = 0;
    int neutralCount = 0;
    for (Tweet resultTweet : resultTweets) {
      Content content = resultTweet.getCde().getContent();
      Message2 message = resultTweet.getMessage();
      message.setLineId(aLineId);
      message.setTwitterId(aTwitterId);
      if (content != null) {
        Sentiment sentiment = content.getSentiment();
        List<Evidence> evidences = sentiment.getEvidence();
        if (evidences != null) {
          for (Evidence evidence : evidences) {
            if (evidence != null) {

              for (int i = 0; i < collectEvidence.size(); i++) {
                if (collectEvidence.get(i).getSentimentTerm()
                    .equalsIgnoreCase(evidence.getSentimentTerm())) {
                  evidence.setSize(collectEvidence.get(i).getSize() + 1);
                  collectEvidence.remove(i);
                }
              }
              collectMessage.add(message);
              evidence.setId(aTwitterId);
              collectEvidence.add(evidence);
            }
          }
        }
        // if (sentiment.getPolarity().equals(POSITIVE)) {
        //   posittiveCount++;
        //   sentimentPositivValue.setText(String.valueOf(posittiveCount));
        // }
        // if (sentiment.getPolarity().equals(NEGATIVE)) {
        //   negativeCount++;
        //   sentimentNegativeValue.setText(String.valueOf(negativeCount));
        // }
        // if (sentiment.getPolarity().equals(NEUTRAL)) {
        //   neutralCount++;
        //   sentimentNeutralValue.setText(String.valueOf(neutralCount));
        // }
      }
    }
  }

}
