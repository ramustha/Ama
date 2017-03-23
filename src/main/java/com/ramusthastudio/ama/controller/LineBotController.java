package com.ramusthastudio.ama.controller;

import com.google.gson.Gson;
import com.ibm.watson.developer_cloud.personality_insights.v3.PersonalityInsights;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.ConsumptionPreferences;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.Content;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.Profile;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.ProfileOptions;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.Trait;
import com.ibm.watson.developer_cloud.util.GsonSingleton;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.ramusthastudio.ama.database.Dao;
import com.ramusthastudio.ama.model.ApiTweets;
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
import com.ramusthastudio.ama.model.UserConsumption;
import com.ramusthastudio.ama.model.UserLine;
import com.ramusthastudio.ama.model.UserTwitter;
import com.ramusthastudio.ama.util.StickerHelper;
import com.ramusthastudio.ama.util.Twitter4JHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import static com.ramusthastudio.ama.util.BotHelper.JOIN;
import static com.ramusthastudio.ama.util.BotHelper.KEY_AMA;
import static com.ramusthastudio.ama.util.BotHelper.KEY_FRIEND;
import static com.ramusthastudio.ama.util.BotHelper.KEY_PERSONALITY;
import static com.ramusthastudio.ama.util.BotHelper.KEY_SUMMARY;
import static com.ramusthastudio.ama.util.BotHelper.KEY_TWITTER;
import static com.ramusthastudio.ama.util.BotHelper.LEAVE;
import static com.ramusthastudio.ama.util.BotHelper.MESSAGE;
import static com.ramusthastudio.ama.util.BotHelper.MESSAGE_TEXT;
import static com.ramusthastudio.ama.util.BotHelper.POSTBACK;
import static com.ramusthastudio.ama.util.BotHelper.SOURCE_GROUP;
import static com.ramusthastudio.ama.util.BotHelper.SOURCE_ROOM;
import static com.ramusthastudio.ama.util.BotHelper.SOURCE_USER;
import static com.ramusthastudio.ama.util.BotHelper.TWITTER_FALSE;
import static com.ramusthastudio.ama.util.BotHelper.TWITTER_NO;
import static com.ramusthastudio.ama.util.BotHelper.TWITTER_SENTIMENT;
import static com.ramusthastudio.ama.util.BotHelper.TWITTER_TRUE;
import static com.ramusthastudio.ama.util.BotHelper.TWITTER_YES;
import static com.ramusthastudio.ama.util.BotHelper.UNFOLLOW;
import static com.ramusthastudio.ama.util.BotHelper.confirmTwitterMessage;
import static com.ramusthastudio.ama.util.BotHelper.generateRandom;
import static com.ramusthastudio.ama.util.BotHelper.getUserProfile;
import static com.ramusthastudio.ama.util.BotHelper.greetingMessage;
import static com.ramusthastudio.ama.util.BotHelper.greetingMessageGroup;
import static com.ramusthastudio.ama.util.BotHelper.instructionSentimentMessage;
import static com.ramusthastudio.ama.util.BotHelper.instructionTweetsMessage;
import static com.ramusthastudio.ama.util.BotHelper.profileUserMessage;
import static com.ramusthastudio.ama.util.BotHelper.pushMessage;
import static com.ramusthastudio.ama.util.BotHelper.replayMessage;
import static com.ramusthastudio.ama.util.BotHelper.stickerMessage;
import static com.ramusthastudio.ama.util.BotHelper.talkMessageGroup;
import static com.ramusthastudio.ama.util.BotHelper.unfollowMessage;
import static com.ramusthastudio.ama.util.StickerHelper.JAMES_STICKER_CHEERS;
import static com.ramusthastudio.ama.util.StickerHelper.JAMES_STICKER_SAD_PRAY;
import static com.ramusthastudio.ama.util.StickerHelper.JAMES_STICKER_SHOCK;
import static com.ramusthastudio.ama.util.StickerHelper.JAMES_STICKER_USELESS;

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
  @Autowired
  PersonalityInsights fPersonalityInsights;

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
      String sourceType = source.type();

      switch (sourceType) {
        case SOURCE_USER:
          sourceUserProccess(eventType, replayToken, timestamp, message, postback, userId);
          break;
        case SOURCE_GROUP:
          sourceGroupProccess(eventType, replayToken, postback, message, source);
          break;
        case SOURCE_ROOM:
          sourceGroupProccess(eventType, replayToken, postback, message, source);
          break;
      }
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  private void sourceGroupProccess(String aEventType, String aReplayToken, Postback aPostback, Message aMessage, Source aSource) {
    try {
      switch (aEventType) {
        case LEAVE:
          break;
        case JOIN:
          LOG.info("Greeting Message join group");
          greetingMessageGroup(fChannelAccessToken, aSource.groupId());
          instructionSentimentMessage(fChannelAccessToken, aSource.groupId());
          break;
        case MESSAGE:
          if (aMessage.type().equals(MESSAGE_TEXT)) {
            String text = aMessage.text();
            Pattern word = Pattern.compile(KEY_AMA);
            Matcher match = word.matcher(text);

            if (text.toLowerCase().startsWith(KEY_TWITTER)) {
              String screenName = text.substring(KEY_TWITTER.length(), text.length()).trim();

              if (screenName.length() > 3) {
                LOG.info("Start find user on database..." + screenName);
                UserTwitter userTwitter = fDao.getUserTwitterById(screenName);
                LOG.info("end find user on database..." + userTwitter);

                if (userTwitter != null) {
                  LOG.info("Display from database...");
                  profileUserMessage(fChannelAccessToken, aSource.groupId(), userTwitter);
                } else {
                  try {
                    User twitterUser = fTwitterHelper.checkUsers(screenName);
                    LOG.info("Display from twitter server...");
                    profileUserMessage(fChannelAccessToken, aSource.groupId(), twitterUser);
                    LOG.info("Start adding user...");
                    fDao.setUserTwitter(twitterUser);
                    LOG.info("End adding user...");
                  } catch (Exception aE) {
                    LOG.error("Getting twitter info error message : " + aE.getMessage());
                  }
                }
              } else {
                replayMessage(fChannelAccessToken, aReplayToken, "Yakin id nya udah bener ? coba cek lagi id nya...");
              }
            } else if (text.toLowerCase().startsWith(TWITTER_SENTIMENT)) {
              String sentiment = text.substring(TWITTER_SENTIMENT.length(), text.length()).trim();
              if (sentiment.length() > 3) {
                List<Message2> message2 = fDao.getUserMessageByTwitterId(sentiment);
                List<Evidence> evidence = fDao.getUserEvidenceByMessageId(sentiment);
                if (message2.size() > 0 && evidence.size() > 0) {
                  LOG.info("Start find sentiment from database...");
                  pushSentiment(aReplayToken, aSource.groupId(), message2, evidence);
                  LOG.info("End find sentiment from database...");
                } else {
                  sentimentService(aReplayToken, aSource.groupId(), sentiment);
                }
              } else {
                replayMessage(fChannelAccessToken, aReplayToken, "hmmm...");
              }

            } else if (match.find()) {
              talkMessageGroup(fChannelAccessToken, aSource.groupId());
              instructionSentimentMessage(fChannelAccessToken, aSource.groupId());
            }
          }
          break;
        case POSTBACK:
          String pd = aPostback.data();

          if (pd.toLowerCase().startsWith(KEY_TWITTER)) {
            String sentiment = pd.substring(KEY_TWITTER.length(), pd.length()).trim();

            List<Message2> message2 = fDao.getUserMessageByTwitterId(sentiment);
            List<Evidence> evidence = fDao.getUserEvidenceByMessageId(sentiment);
            if (message2.size() > 0 && evidence.size() > 0) {
              LOG.info("Start find sentiment from database...");
              pushSentiment(aReplayToken, aSource.groupId(), message2, evidence);
              LOG.info("End find sentiment from database...");
            } else {
              sentimentService(aReplayToken, aSource.groupId(), sentiment);
            }
          }
          break;
      }
    } catch (IOException aE) { LOG.error("Message {}", aE.getMessage()); }
  }

  private void sourceUserProccess(String aEventType, String aReplayToken, long aTimestamp, Message aMessage, Postback aPostback, String aUserId) {
    try {
      LOG.info("Start find UserProfileResponse on database...");
      UserProfileResponse profile = getUserProfile(fChannelAccessToken, aUserId);
      UserLine mUserLine = fDao.getUserLineById(profile.getUserId());
      if (mUserLine == null) {
        LOG.info("Start save user line to database...");
        fDao.setUserLine(profile);
      }
      LOG.info("End find UserProfileResponse on database..." + mUserLine);
    } catch (IOException ignored) {}

    try {
      switch (aEventType) {
        case UNFOLLOW:
          unfollowMessage(fChannelAccessToken, aUserId);
          break;
        case FOLLOW:
          LOG.info("Greeting Message");
          greetingMessage(fChannelAccessToken, aUserId);
          instructionSentimentMessage(fChannelAccessToken, aUserId);
          confirmTwitterMessage(fChannelAccessToken, aUserId);
          break;
        case MESSAGE:
          if (aMessage.type().equals(MESSAGE_TEXT)) {
            String text = aMessage.text();
            boolean isValid = true;

            Pattern keyTwitter = Pattern.compile(KEY_TWITTER);
            Matcher matchTwitter = keyTwitter.matcher(text);

            Pattern keyFriend = Pattern.compile(KEY_FRIEND);
            Matcher matchFriend = keyFriend.matcher(text);

            if (text.toLowerCase().startsWith(KEY_TWITTER)) {
              String screenName = text.substring(KEY_TWITTER.length(), text.length()).trim();

              if (screenName.length() > 3) {
                LOG.info("Start find user on database..." + screenName);
                UserTwitter userTwitter = fDao.getUserTwitterById(screenName);
                LOG.info("end find user on database..." + userTwitter);

                if (userTwitter != null) {
                  LOG.info("Display from database...");
                  profileUserMessage(fChannelAccessToken, aUserId, userTwitter);
                } else {
                  try {
                    User twitterUser = fTwitterHelper.checkUsers(screenName);
                    LOG.info("Display from twitter server..." + twitterUser.getName());
                    profileUserMessage(fChannelAccessToken, aUserId, twitterUser);
                    LOG.info("Start adding user...");
                    fDao.setUserTwitter(twitterUser);
                    LOG.info("End adding user...");
                  } catch (Exception aE) {
                    LOG.error("Getting twitter info error message : " + aE.getMessage());
                  }

                  confirmTwitterMessage(fChannelAccessToken, aUserId, "Bener ini twitter nya ?", TWITTER_TRUE + screenName, TWITTER_FALSE);
                }
              } else {
                replayMessage(fChannelAccessToken, aReplayToken, "Yakin id nya udah bener ? coba cek lagi id nya...");
              }
            } else if (text.toLowerCase().startsWith(TWITTER_SENTIMENT)) {
              String sentiment = text.substring(TWITTER_SENTIMENT.length(), text.length()).trim();
              if (sentiment.length() > 3) {
                List<Message2> message2 = fDao.getUserMessageByTwitterId(sentiment);
                List<Evidence> evidence = fDao.getUserEvidenceByMessageId(sentiment);
                if (message2.size() > 0 && evidence.size() > 0) {
                  LOG.info("Start find sentiment from database...");
                  pushSentiment(aReplayToken, aUserId, message2, evidence);
                  LOG.info("End find sentiment from database...");
                } else {
                  sentimentService(aReplayToken, aUserId, sentiment);
                }
              } else {
                replayMessage(fChannelAccessToken, aReplayToken, "hmmm...");
              }

            } else if (text.toLowerCase().startsWith(KEY_PERSONALITY)) {
              String personality = text.substring(KEY_PERSONALITY.length(), text.length()).trim();
              replayMessage(fChannelAccessToken, aReplayToken, personality);

            } else if (text.toLowerCase().startsWith(KEY_SUMMARY)) {
              String summary = text.substring(KEY_SUMMARY.length(), text.length()).trim();
              replayMessage(fChannelAccessToken, aReplayToken, summary);

            } else {
              isValid = false;
              replayMessage(fChannelAccessToken, aReplayToken, aMessage.text() + " ?");
            }

            if (isValid) {
              LOG.info("isValid...");
              UserChat userChat = fDao.getUserChatById(aUserId);
              if (userChat != null) {
                LOG.info("Start updating chat history...");
                fDao.updateUserChat(new UserChat(aUserId, text, aTimestamp));
              } else {
                LOG.info("Start saving chat history...");
                fDao.setUserChat(new UserChat(aUserId, text, aTimestamp));
              }
            } else {
              LOG.info("isNotValid...");
              UserChat userChat = fDao.getUserChatById(aUserId);
              if (userChat != null) {
                LOG.info("Start updating false count...");
                int count = userChat.getFalseCount();
                if (count == 2) {
                  stickerMessage(fChannelAccessToken, aUserId, new StickerHelper.StickerMsg(JAMES_STICKER_USELESS));
                  pushMessage(fChannelAccessToken, aUserId, "Aku gak ngerti nih kamu ngomong apa, " +
                      "aku ini cuma bot yang bisa membaca sentiment lewat twitter, jadi jangan tanya yang aneh aneh dulu yah");
                  fDao.updateUserChat(new UserChat(aUserId, text, aTimestamp, 0));
                } else {
                  count++;
                  fDao.updateUserChat(new UserChat(aUserId, text, aTimestamp, count));
                }
                LOG.info("count..." + count);
              }
            }
          } else {
            pushMessage(fChannelAccessToken, aUserId, "Aku gak ngerti nih, " +
                "aku ini cuma bot yang bisa membaca sentiment lewat twitter, jadi jangan tanya yang aneh aneh dulu yah");
          }
          break;
        case POSTBACK:
          String pd = aPostback.data();

          if (pd.startsWith(TWITTER_YES)) {
            stickerMessage(fChannelAccessToken, aUserId, new StickerHelper.StickerMsg(JAMES_STICKER_CHEERS));
            replayMessage(fChannelAccessToken, aReplayToken, "Coba kamu tulis sentiment indonesia");
          } else if (pd.startsWith(TWITTER_NO)) {
            stickerMessage(fChannelAccessToken, aUserId, new StickerHelper.StickerMsg(JAMES_STICKER_SHOCK));
            replayMessage(fChannelAccessToken, aReplayToken, "Hari gini gak punya twitter ?");
          } else if (pd.startsWith(TWITTER_TRUE)) {
            String screenName = pd.substring(TWITTER_TRUE.length(), pd.length());
            UserTwitter userTwitter = fDao.getUserTwitterById(screenName);
            if (userTwitter != null) {
              List<Message2> message2 = fDao.getUserMessageByTwitterId(userTwitter.getId());
              List<Evidence> evidence = fDao.getUserEvidenceByMessageId(userTwitter.getId());
              if (message2.size() > 0 && evidence.size() > 0) {
                LOG.info("Start find sentiment from database...");
                pushSentiment(aReplayToken, aUserId, message2, evidence);
                LOG.info("End find sentiment from database...");
              } else {
                sentimentService(aReplayToken, aUserId, userTwitter);
              }
            } else {
              replayMessage(fChannelAccessToken, aReplayToken, "Hmmm... kayak nya jarang nge tweets nih, aku gak bisa bantuin...\n" +
                  " aku cuma bisa bantuin kalau tweets nya bahasa Inggris, Jerman, Perancis, dan Spanyol");
              stickerMessage(fChannelAccessToken, aUserId, new StickerHelper.StickerMsg(JAMES_STICKER_SAD_PRAY));
            }

          } else if (pd.startsWith(TWITTER_FALSE)) {
            replayMessage(fChannelAccessToken, aReplayToken, "Salah ? trus ini siapa ?");
          } else if (pd.toLowerCase().startsWith(KEY_TWITTER)) {
            String sentiment = pd.substring(KEY_TWITTER.length(), pd.length()).trim();

            List<Message2> message2 = fDao.getUserMessageByTwitterId(sentiment);
            List<Evidence> evidence = fDao.getUserEvidenceByMessageId(sentiment);
            if (message2.size() > 0 && evidence.size() > 0) {
              LOG.info("Start find sentiment from database...");
              pushSentiment(aReplayToken, aUserId, message2, evidence);
              LOG.info("End find sentiment from database...");
            } else {
              sentimentService(aReplayToken, aUserId, sentiment);
            }
          } else if (pd.startsWith(KEY_PERSONALITY)) {
            String personalityCandidate = pd.substring(KEY_PERSONALITY.length(), pd.length()).trim();
            replayMessage(fChannelAccessToken, aReplayToken, personalityCandidate);

          } else if (pd.startsWith(KEY_SUMMARY)) {
            try {
              String summaryCandidate = pd.substring(KEY_SUMMARY.length(), pd.length()).trim();

              StringBuilder consumptionBuilder = new StringBuilder();
              StringBuilder likelyBuilder = new StringBuilder("Likely to...\n");
              StringBuilder unlikelyBuilder = new StringBuilder("Unlikely to...\n");
              int maxLike = 0;
              int maxUnLike = 0;

              List<UserConsumption> userConsumptionsLike = fDao.getUserConsumptionByTwitterIdAndScore(summaryCandidate, 1);
              List<UserConsumption> userConsumptionsUnLike = fDao.getUserConsumptionByTwitterIdAndScore(summaryCandidate, 0);
              if (userConsumptionsLike.size() > 0 && userConsumptionsUnLike.size() > 0) {
                LOG.info("Start find userConsumptionsLike from database...");

                List<UserConsumption> userConsumptionsMiddle = fDao.getUserConsumptionByTwitterIdAndScore(summaryCandidate, 0.5);
                StringBuilder middleBuilder = null;
                if (userConsumptionsMiddle.size() > 0) {
                  middleBuilder = new StringBuilder("Like or unlike sometimes...\n");
                  ArrayList<String> mm = generateRandomLikeConsumption(userConsumptionsLike);
                  for (String s : mm) { middleBuilder.append("\n-").append(s); }
                }

                ArrayList<String> likeConsumption = generateRandomLikeConsumption(userConsumptionsLike);
                ArrayList<String> unLikeConsumption = generateRandomLikeConsumption(userConsumptionsUnLike);
                for (String s : likeConsumption) { likelyBuilder.append("\n-").append(s); }
                for (String s : unLikeConsumption) { unlikelyBuilder.append("\n-").append(s); }

                consumptionBuilder.append(likelyBuilder).append("\n\n");
                if (middleBuilder != null) {
                  consumptionBuilder.append(middleBuilder).append("\n\n");
                }
                consumptionBuilder.append(unlikelyBuilder);

              } else {
                LOG.info("Start find userConsumptionsLike from service...");
                Content content = GsonSingleton.getGson().fromJson(fTwitterHelper.getTweets(summaryCandidate, TWEETS_STEP), Content.class);
                ProfileOptions options = new ProfileOptions.Builder()
                    .contentItems(content.getContentItems())
                    .consumptionPreferences(true)
                    .rawScores(true)
                    .build();
                Profile personality = fPersonalityInsights.getProfile(options).execute();
                List<Trait> personalities = personality.getPersonality();
                List<Trait> needs = personality.getNeeds();
                List<Trait> values = personality.getValues();
                List<ConsumptionPreferences> consumtionPreferences = personality.getConsumptionPreferences();
                int removePrefix = "Likely to ".length();
                for (ConsumptionPreferences cp : consumtionPreferences) {
                  UserConsumption uc = new UserConsumption();
                  uc.setTwitterId(summaryCandidate);
                  uc.setConsumptionCategory(cp.getCategoryId());
                  for (ConsumptionPreferences.ConsumptionPreference consumptionPreference : cp.getConsumptionPreferences()) {
                    double score = consumptionPreference.getScore();
                    String name = consumptionPreference.getName().substring(removePrefix, consumptionPreference.getName().length());
                    uc.setConsumptionName(name).setConsumptionScore(score);
                    LOG.info("Start saving userConsumptionsLike to database...");
                    fDao.setUserConsumption(uc);

                    if (score == 1 && maxLike != 5) {
                      likelyBuilder.append("\n").append(name);
                      maxLike++;
                    } else if (maxUnLike != 5) {
                      unlikelyBuilder.append("\n").append(name);
                      maxUnLike++;
                    }
                  }
                }
                consumptionBuilder.append(likelyBuilder).append("\n\n").append(unlikelyBuilder);
              }
              pushMessage(fChannelAccessToken, aUserId, consumptionBuilder.toString());
              if (generateRandom(0, 1) == 1) {
                pushMessage(fChannelAccessToken, aUserId, "Ngerti kan maksudnya ?\n\n" +
                    "Untuk sekarang aku cuma bisa kasih info pake bahasa inggris nih...");
              }
            } catch (Exception aE) {
              LOG.error("Exception when reading tweets..." + aE.getMessage());
            }

          }
          break;
      }
    } catch (IOException aE) { LOG.error("Message {}", aE.getMessage()); }
  }

  private void sentimentService(String aReplayToken, String aUserId, UserTwitter aUserTwitter) throws IOException {
    LOG.info("Start sentiment service...");
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
    if (generateRandom(0, 1) == 1) { instructionTweetsMessage(fChannelAccessToken, aUserId); }
    LOG.info("End sentiment service...");
  }

  private void sentimentService(String aReplayToken, String aUserId, String aSentiment) throws IOException {
    LOG.info("Start sentiment service...");
    Call<ApiTweets> tweets = fSentimentTweetService.apiTweets(aSentiment, MAX_TWEETS);
    Response<ApiTweets> exec = tweets.execute();
    ApiTweets apiTweets = exec.body();
    // Search resultSearch = apiTweets.getSearch();
    List<Tweet> resultTweets = apiTweets.getTweets();
    // Related resultRelated = apiTweets.getRelated();
    polarityProcess(aUserId, aSentiment, resultTweets);
    for (Message2 message2 : collectMessage) { fDao.setUserMessage(message2); }
    for (Evidence evidence : collectEvidence) { fDao.setUserEvidence(evidence); }
    pushSentiment(aReplayToken, aUserId, collectMessage, collectEvidence);
    if (generateRandom(0, 1) == 1) { instructionTweetsMessage(fChannelAccessToken, aUserId); }
    LOG.info("End sentiment service...");
  }

  private void pushSentiment(String aReplayToken, String aUserId, List<Message2> aMessage2, List<Evidence> aEvidence) throws IOException {
    StringBuilder builderPositive = new StringBuilder("Positif...\n\n");
    StringBuilder builderNegative = new StringBuilder("Negatif...\n\n");
    int positivelength = builderPositive.length();
    int negativelength = builderNegative.length();
    ArrayList<String> positive = new ArrayList<>();
    ArrayList<String> negative = new ArrayList<>();
    for (Evidence evidence : aEvidence) {
      String term = evidence.getSentimentTerm();
      if (evidence.getPolarity().equalsIgnoreCase("POSITIVE") && !positive.contains(term)) {
        positive.add(term);
      } else if (evidence.getPolarity().equalsIgnoreCase("NEGATIVE") && !negative.contains(term)) {
        negative.add(term);
      }
    }

    for (String s : positive) { builderPositive.append(s).append(", "); }
    for (String s : positive) { builderNegative.append(s).append(", "); }

    StringBuilder b = new StringBuilder("Ini kata orang lain yah, bukan kata aku...\n\n");
    if (aEvidence.size() > 0) {

      if (positivelength != builderPositive.length()) {
        b.append(builderPositive.toString()).append("\n\n");
      }
      if (negativelength != builderNegative.length()) {
        b.append(builderNegative.toString());
      }

      replayMessage(fChannelAccessToken, aReplayToken, b.toString());
    } else {
      replayMessage(fChannelAccessToken, aReplayToken, "hmm.. belum ada sentiment nih, gak begitu populer kayaknya");
    }
  }

  private void polarityProcess(String aLineId, String aTwitterId, List<Tweet> resultTweets) {
    int posittiveCount = 0;
    int negativeCount = 0;
    int neutralCount = 0;
    for (Tweet resultTweet : resultTweets) {
      com.ramusthastudio.ama.model.Content content = resultTweet.getCde().getContent();
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
                if (collectEvidence.get(i).getSentimentTerm().trim()
                    .equalsIgnoreCase(evidence.getSentimentTerm().trim())) {
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

  private static ArrayList<String> generateRandomLikeConsumption(List<UserConsumption> aUserConsumptions) {
    ArrayList<String> likeConsumption = new ArrayList<>();
    while (likeConsumption.size() < aUserConsumptions.size() / 2) {
      String candidate = aUserConsumptions.get(generateRandom(0, aUserConsumptions.size())).getConsumptionName();
      if (!likeConsumption.contains(candidate)) {
        likeConsumption.add(candidate);
      }
    }
    return likeConsumption;
  }
}
