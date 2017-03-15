package com.ramusthastudio.ama.util;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.Template;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import java.io.IOException;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

public final class BotHelper {
  private static final Logger LOG = LoggerFactory.getLogger(BotHelper.class);
  public static final String DFL_REGION = "ID";

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

  public static final String KW_FIND = "Find";
  public static final String KW_DETAIL = "Detail";
  public static final String KW_STAR = "Star";
  public static final String KW_DETAIL_OVERVIEW = "Overview";
  public static final String KW_NOW_PLAYING = "Now Playing";
  public static final String KW_LATEST = "Latest";
  public static final String KW_POPULAR = "Popular";
  public static final String KW_TOP_RATED = "Top Rated";
  public static final String KW_UPCOMING = "Comming Soon";
  public static final String KW_RECOMMEND = "Recommend";
  public static final String KW_SIMILAR = "Similar";
  public static final String KW_VIDEOS = "Video";
  public static final String KW_PANDUAN = "Panduan";

  public static Response<UserProfileResponse> getUserProfile(String aChannelAccessToken,
      String aUserId) throws IOException {
    return LineMessagingServiceBuilder.create(aChannelAccessToken).build().getProfile(aUserId).execute();
  }

  public static Response<BotApiResponse> replayMessage(String aChannelAccessToken, String aReplayToken,
      String aMsg) throws IOException {
    TextMessage message = new TextMessage(aMsg);
    ReplyMessage pushMessage = new ReplyMessage(aReplayToken, message);
    return LineMessagingServiceBuilder.create(aChannelAccessToken).build().replyMessage(pushMessage).execute();
  }

  public static Response<BotApiResponse> pushMessage(String aChannelAccessToken, String aUserId,
      String aMsg) throws IOException {
    TextMessage message = new TextMessage(aMsg);
    PushMessage pushMessage = new PushMessage(aUserId, message);
    return LineMessagingServiceBuilder.create(aChannelAccessToken).build().pushMessage(pushMessage).execute();
  }

  public static Response<BotApiResponse> multicastMessage(String aChannelAccessToken, Set<String> aUserIds,
      String aMsg) throws IOException {
    TextMessage message = new TextMessage(aMsg);
    Multicast pushMessage = new Multicast(aUserIds, message);
    return LineMessagingServiceBuilder.create(aChannelAccessToken).build().multicast(pushMessage).execute();
  }

  public static Response<BotApiResponse> templateMessage(String aChannelAccessToken, String aUserId,
      Template aTemplate) throws IOException {
    TemplateMessage message = new TemplateMessage("Result", aTemplate);
    PushMessage pushMessage = new PushMessage(aUserId, message);
    return LineMessagingServiceBuilder.create(aChannelAccessToken).build().pushMessage(pushMessage).execute();
  }

  public static Response<BotApiResponse> stickerMessage(String aChannelAccessToken, String aUserId,
      String aPackageId, String stickerId) throws IOException {
    StickerMessage message = new StickerMessage(aPackageId, stickerId);
    PushMessage pushMessage = new PushMessage(aUserId, message);
    return LineMessagingServiceBuilder.create(aChannelAccessToken).build().pushMessage(pushMessage).execute();
  }

  public static void greetingMessage(String aChannelAccessToken, String aUserId) throws IOException {
    stickerMessage(aChannelAccessToken, aUserId, "1", "125");
    UserProfileResponse userProfile = getUserProfile(aChannelAccessToken, aUserId).body();
    String greeting = "Hi " + userProfile.getDisplayName() + "\n";
    greeting += "Terima kasih telah menambahkan saya sebagai teman! \n\n";
    pushMessage(aChannelAccessToken, aUserId, greeting);
  }
}
