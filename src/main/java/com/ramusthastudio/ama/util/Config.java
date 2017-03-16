package com.ramusthastudio.ama.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class Config {
  @Autowired
  Environment mEnv;

  @Bean(name = "line.bot.channelSecret")
  public String getChannelSecret() {
    return mEnv.getProperty("line.bot.channelSecret");
  }

  @Bean(name = "line.bot.channelToken")
  public String getChannelAccessToken() {
    return mEnv.getProperty("line.bot.channelToken");
  }

  @Bean(name = "twitter.consumerKey")
  public String getTwitterConsumerKey() {
    return mEnv.getProperty("twitter.consumerKey");
  }

  @Bean(name = "twitter.consumerSecret")
  public String getTwitterConsumerSecret() {
    return mEnv.getProperty("twitter.consumerSecret");
  }

  @Bean(name = "twitter.accessToken")
  public String getTwitterAccessToken() {
    return mEnv.getProperty("twitter.accessToken");
  }

  @Bean(name = "twitter.accessSecret")
  public String getTwitterAccessSecret() {
    return mEnv.getProperty("twitter.accessSecret");
  }
}
