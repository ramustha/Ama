package com.ramusthastudio.ama.util;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.message.template.Template;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.ramusthastudio.ama.model.UserLine;
import com.ramusthastudio.ama.model.UserTwitter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import twitter4j.User;

import static com.ramusthastudio.ama.util.StickerHelper.JAMES_STICKER_AFRAID;
import static com.ramusthastudio.ama.util.StickerHelper.JAMES_STICKER_TWO_THUMBS;

public final class BotHelper {
  private static final Logger LOG = LoggerFactory.getLogger(BotHelper.class);

  public static final String SOURCE_USER = "user";
  public static final String SOURCE_GROUP = "group";
  public static final String SOURCE_ROOM = "room";

  public static final String JOIN = "join";
  public static final String FOLLOW = "follow";
  public static final String UNFOLLOW = "unfollow";
  public static final String MESSAGE = "message";
  public static final String LEAVE = "leave";
  public static final String POSTBACK = "postback";
  public static final String BEACON = "beacon";

  public static final String MESSAGE_TEXT = "text";
  public static final String MESSAGE_IMAGE = "image";
  public static final String MESSAGE_VIDEO = "video";
  public static final String MESSAGE_AUDIO = "audio";
  public static final String MESSAGE_LOCATION = "location";
  public static final String MESSAGE_STICKER = "sticker";

  public static final String KEY_TWITTER = "twitter";
  public static final String KEY_POSITIVE = "twitter positif";
  public static final String KEY_NEGATIVE = "twitter negatif";
  public static final String KEY_FRIEND = "teman";

  public static final String TWITTER = "twitter:";
  public static final String TWITTER_YES = "yes:";
  public static final String TWITTER_NO = "no:";
  public static final String TWITTER_TRUE = "true:";
  public static final String TWITTER_FALSE = "false:";
  public static final String TWITTER_SENTIMENT = "sentiment";

  public static final String POSITIVE = "POSITIVE";
  public static final String NEGATIVE = "NEGATIVE";
  public static final String NEUTRAL = "NEUTRAL";

  public static UserProfileResponse getUserProfile(String aChannelAccessToken,
      String aUserId) throws IOException {
    LOG.info("getUserProfile...");
    return LineMessagingServiceBuilder
        .create(aChannelAccessToken)
        .build().getProfile(aUserId).execute().body();
  }

  public static Response<BotApiResponse> replayMessage(String aChannelAccessToken, String aReplayToken,
      String aMsg) throws IOException {
    TextMessage message = new TextMessage(aMsg);
    ReplyMessage pushMessage = new ReplyMessage(aReplayToken, message);
    LOG.info("replayMessage...");
    return LineMessagingServiceBuilder
        .create(aChannelAccessToken)
        .build().replyMessage(pushMessage).execute();
  }

  public static Response<BotApiResponse> pushMessage(String aChannelAccessToken, String aUserId,
      String aMsg) throws IOException {
    TextMessage message = new TextMessage(aMsg);
    PushMessage pushMessage = new PushMessage(aUserId, message);
    LOG.info("pushMessage...");
    return LineMessagingServiceBuilder
        .create(aChannelAccessToken)
        .build().pushMessage(pushMessage).execute();
  }

  public static Response<BotApiResponse> multicastMessage(String aChannelAccessToken, Set<String> aUserIds,
      String aMsg) throws IOException {
    TextMessage message = new TextMessage(aMsg);
    Multicast pushMessage = new Multicast(aUserIds, message);
    LOG.info("multicastMessage...");
    return LineMessagingServiceBuilder
        .create(aChannelAccessToken)
        .build().multicast(pushMessage).execute();
  }

  public static Response<BotApiResponse> templateMessage(String aChannelAccessToken, String aUserId,
      Template aTemplate) throws IOException {
    TemplateMessage message = new TemplateMessage("Result", aTemplate);
    PushMessage pushMessage = new PushMessage(aUserId, message);
    LOG.info("templateMessage...");
    return LineMessagingServiceBuilder
        .create(aChannelAccessToken)
        .build().pushMessage(pushMessage).execute();
  }

  public static Response<BotApiResponse> stickerMessage(String aChannelAccessToken, String aUserId,
      StickerHelper.StickerMsg aSt) throws IOException {
    StickerMessage message = new StickerMessage(aSt.pkgId(), aSt.id());
    PushMessage pushMessage = new PushMessage(aUserId, message);
    LOG.info("stickerMessage...");
    return LineMessagingServiceBuilder
        .create(aChannelAccessToken)
        .build().pushMessage(pushMessage).execute();
  }

  public static Response<BotApiResponse> profileUserMessage(String aChannelAccessToken, String aUserId, UserTwitter aModel) throws IOException {
    String desc = aModel.getDescription();
    if (aModel.getDescription().isEmpty()) {
      desc = "Gak nyantumin deskripsi";
    }
    ButtonsTemplate template = new ButtonsTemplate(
        aModel.getOriginalProfileImageUrlHttps(),
        aModel.getDisplayName(),
        desc,
        Collections.singletonList(
            new PostbackAction("Apa kata orang ?", TWITTER_SENTIMENT + " " + aModel.getId())
        ));

    return templateMessage(aChannelAccessToken, aUserId, template);
  }

  public static Response<BotApiResponse> profileUserMessage(String aChannelAccessToken, String aUserId, User aUser) throws IOException {
    String desc = aUser.getDescription();
    if (aUser.getDescription().isEmpty()) {
      desc = "Gak nyantumin deskripsi";
    }
    ButtonsTemplate template = new ButtonsTemplate(
        aUser.getProfileImageURL(),
        aUser.getScreenName(),
        desc,
        Collections.singletonList(
            new PostbackAction("Apa kata orang ?", KEY_TWITTER + " " + aUser.getName())
        ));

    return templateMessage(aChannelAccessToken, aUserId, template);
  }

  public static Response<BotApiResponse> buttonMessage(String aChannelAccessToken, UserLine aUserLine) throws IOException {
    String status = aUserLine.getStatusMessage() == null ? "Gak nyantumin status" : aUserLine.getStatusMessage();

    LOG.info("buttonMessage {} " + aUserLine);
    ButtonsTemplate template = new ButtonsTemplate(
        aUserLine.getPictureUrl(), aUserLine.getDisplayName(), status,
        Collections.singletonList(
            new URIAction("Lihat profile ?", aUserLine.getPictureUrl()))
    );
    return templateMessage(aChannelAccessToken, aUserLine.getUserId(), template);
  }

  public static void greetingMessage(String aChannelAccessToken, String aUserId) throws IOException {
    UserProfileResponse userProfile = getUserProfile(aChannelAccessToken, aUserId);
    String greeting = "Hi " + userProfile.getDisplayName() + "\n";
    greeting += "Terima kasih telah menambahkan saya sebagai teman!\n";
    greeting += "Kenalin, aku AMA bot yang bisa membaca sentiment lewat twitter,";
    greeting += "sentiment atau pendapat orang tentang apapun di dalam dunia twitter";
    stickerMessage(aChannelAccessToken, aUserId, new StickerHelper.StickerMsg(JAMES_STICKER_TWO_THUMBS));
    pushMessage(aChannelAccessToken, aUserId, greeting);
  }

  public static void unfollowMessage(String aChannelAccessToken, String aUserId) throws IOException {
    UserProfileResponse userProfile = getUserProfile(aChannelAccessToken, aUserId);
    String greeting = "Hi " + userProfile.getDisplayName() + "\n";
    greeting += "Kenapa kamu unfollow aku? jahat !!!";
    pushMessage(aChannelAccessToken, aUserId, greeting);
  }

  public static void instructionTweetsMessage(String aChannelAccessToken, String aUserId) throws IOException {
    String greeting = "Contoh jika kamu pengen tau nih pendapat orang lain tentang indonesia,";
    greeting += "Kamu tinggal bilang sentiment indonesia, ";
    greeting += "nanti aku kumpulin infonya terus aku kasih tau ke kamu apa pendapat orang lain tentang indonesia";
    pushMessage(aChannelAccessToken, aUserId, greeting);
  }

  public static void errorHandleTweetsMessage(String aChannelAccessToken, String aUserId) throws IOException {
    UserProfileResponse userProfile = getUserProfile(aChannelAccessToken, aUserId);
    String greeting = "Hi " + userProfile.getDisplayName() + "\n";
    greeting += "Ah kamu jarang nge-tweets nih\n";
    greeting += "Aku gak bisa tau kalau kamu jarang nge-tweets\n";
    stickerMessage(aChannelAccessToken, aUserId, new StickerHelper.StickerMsg(JAMES_STICKER_AFRAID));
    pushMessage(aChannelAccessToken, aUserId, greeting);
  }

  public static Response<BotApiResponse> confirmTwitterMessage(String aChannelAccessToken, String aUserId) throws IOException {
    ConfirmTemplate template = new ConfirmTemplate("Kamu punya twitter ?", Arrays.asList(
        new PostbackAction("Punya", TWITTER_YES),
        new PostbackAction("nggak", TWITTER_NO)
    ));
    return templateMessage(aChannelAccessToken, aUserId, template);
  }

  public static Response<BotApiResponse> confirmTwitterMessage(String aChannelAccessToken, String aUserId, String aMsg, String aDataYes, String aDataNo) throws IOException {
    ConfirmTemplate template = new ConfirmTemplate(aMsg, Arrays.asList(
        new PostbackAction("Bener", aDataYes),
        new PostbackAction("Salah", aDataNo)
    ));
    return templateMessage(aChannelAccessToken, aUserId, template);
  }

  public static int generateRandom(int min, int max) {
    Random r = new Random();
    return r.nextInt(max - min) + min;
  }

  public static String predictWord(String aText, String aFind) {
    Pattern word = Pattern.compile(aFind);
    Matcher match = word.matcher(aText);
    String result = "";
    while (match.find()) {
      String predictAfterKey = removeAnySymbol(aText.substring(match.end(), aText.length())).trim();

      if (predictAfterKey.length() > 0) {
        if (predictAfterKey.contains(" ")) {
          String[] predictAfterKeySplit = predictAfterKey.split(" ");
          result = predictAfterKeySplit[0];
        } else {
          result = predictAfterKey;
        }
        return result;
      }
    }
    return result;
  }

  public static String removeAnySymbol(String s) {
    Pattern pattern = Pattern.compile("[^a-z A-Z^0-9]");
    Matcher matcher = pattern.matcher(s);
    return matcher.replaceAll(" ");
  }
}
