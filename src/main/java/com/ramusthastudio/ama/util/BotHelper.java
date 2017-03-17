package com.ramusthastudio.ama.util;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.message.template.Template;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.ramusthastudio.ama.database.Dao;
import com.ramusthastudio.ama.model.UserLine;
import com.ramusthastudio.ama.model.UserModel;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
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

  public static final String TWITTER = "twitter:";
  public static final String TWITTER_YES = "twitter_yes";
  public static final String TWITTER_NO = "twitter_no";
  public static final String TWITTER_TRUE = "twitter_true";
  public static final String TWITTER_FALSE = "twitter_false";
  public static final String TWITTER_SENTIMENT = "twitter_sentiment";

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

  public static Response<BotApiResponse> profileUserMessage(String aChannelAccessToken, String aUserId, UserModel aModel) throws IOException {
    String desc = aModel.getDescription();
    if (aModel.getDescription().isEmpty()) {
      desc = "Gak nyantumin deskripsi";
    }
    ButtonsTemplate template = new ButtonsTemplate(
        aModel.getOriginalProfileImageUrlHttps(),
        aModel.getScreenName(),
        desc,
        Collections.singletonList(
            new PostbackAction("Apa kata orang ?", TWITTER_SENTIMENT + " " + aModel.getId())
        ));

    return templateMessage(aChannelAccessToken, aUserId, template);
  }

  public static Response<BotApiResponse> profileUserMessage(String aChannelAccessToken, String aUserId, User aModel) throws IOException {
    String desc = aModel.getDescription();
    if (aModel.getDescription().isEmpty()) {
      desc = "Gak nyantumin deskripsi";
    }
    ButtonsTemplate template = new ButtonsTemplate(
        aModel.getOriginalProfileImageURLHttps(),
        aModel.getScreenName(),
        desc,
        Collections.singletonList(
            new PostbackAction("Apa kata orang ?", TWITTER_SENTIMENT + " " + aModel.getId())
        ));

    return templateMessage(aChannelAccessToken, aUserId, template);
  }

  public static void greetingMessage(String aChannelAccessToken, String aUserId) throws IOException {
    UserProfileResponse userProfile = getUserProfile(aChannelAccessToken, aUserId);
    String greeting = "Hi " + userProfile.getDisplayName() + "\n";
    greeting += "Terima kasih telah menambahkan saya sebagai teman!";
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
    UserProfileResponse userProfile = getUserProfile(aChannelAccessToken, aUserId);
    String greeting = "Hi " + userProfile.getDisplayName() + "\n";
    greeting += "Kamu mau tahu yang orang lain pikirin tentang kamu ?\n";
    greeting += "Aku bisa tau loh... asalkan kamu sering nge-tweets\n";
    greeting += "coba tulis id twitter nya , contoh 'twitter:@idtwitter'";
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

  public static Response<BotApiResponse> confirmTwitterMessage(String aChannelAccessToken, String aUserId, String aMsg, String aYes, String aNo) throws IOException {
    ConfirmTemplate template = new ConfirmTemplate(aMsg, Arrays.asList(
        new PostbackAction("Bener", aYes),
        new PostbackAction("Salah", aNo)
    ));
    return templateMessage(aChannelAccessToken, aUserId, template);
  }
}
