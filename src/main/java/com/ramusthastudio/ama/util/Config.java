package com.ramusthastudio.ama.util;

import com.ibm.watson.developer_cloud.personality_insights.v3.PersonalityInsights;
import com.ramusthastudio.ama.database.Dao;
import com.ramusthastudio.ama.database.DaoImpl;
import com.ramusthastudio.ama.model.SentimentTweetService;
import javax.sql.DataSource;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ibm.watson.developer_cloud.personality_insights.v3.PersonalityInsights.VERSION_DATE_2016_10_19;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
@PropertySource("classpath:application.properties")
public class Config {
  @Bean public DataSource getDataSource() {
    String dbUrl = System.getenv("JDBC_DATABASE_URL");
    String username = System.getenv("JDBC_DATABASE_USERNAME");
    String password = System.getenv("JDBC_DATABASE_PASSWORD");

    DriverManagerDataSource ds = new DriverManagerDataSource();
    ds.setDriverClassName("org.postgresql.Driver");
    ds.setUrl(dbUrl);
    ds.setUsername(username);
    ds.setPassword(password);

    return ds;
  }

  @Bean public Dao getPersonDao() { return new DaoImpl(getDataSource()); }
  @Bean(name = "line.bot.channelSecret")
  public String getChannelSecret() { return System.getenv("line.bot.channelSecret"); }
  @Bean(name = "line.bot.channelToken")
  public String getChannelAccessToken() { return System.getenv("line.bot.channelToken"); }

  @Bean
  public static SentimentTweetService getTwitterService() {
    String dbUrl = System.getenv("oauth.pi.url");
    String username = System.getenv("oauth.pi.username");
    String password = System.getenv("oauth.pi.password");
    String host = System.getenv("oauth.pi.host");
    String port = System.getenv("oauth.pi.port");

    OkHttpClient client = new OkHttpClient()
        .newBuilder()
        .addNetworkInterceptor(
            chain -> {
              Request request = chain.request();

              String authToken = Credentials.basic(username, password);
              Request.Builder requestBuilder = request.newBuilder()
                  .header(AUTHORIZATION, authToken);

              Request newRequest = requestBuilder.build();
              return chain.proceed(newRequest);
            }
        ).build();

    Retrofit retrofit = new Retrofit.Builder()
        .client(client)
        .baseUrl(dbUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    return retrofit.create(SentimentTweetService.class);
  }

  @Bean
  public static PersonalityInsights getPersonalityService()
  {
    PersonalityInsights service = new PersonalityInsights(VERSION_DATE_2016_10_19);
    service.setUsernameAndPassword(System.getenv("oauth.ibmpi.username"), System.getenv("oauth.ibmpi.password"));
    return service;
  }
}
