package com.ramusthastudio.ama.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SentimentTweetService {
  @GET("api/v1/messages/search?")
  Call<ApiTweets> apiTweets(@Query("q") String aQuery, @Query("size") int aSize);

  @GET("api/v1/messages/search?")
  Call<ApiTweets> apiTweets(@Query("q") String aQuery, @Query("from") int aFrom, @Query("size") int aSize);

  @GET("api/v1/messages/count?")
  Call<ApiTweets> apiTweetCount(@Query("q") String aQuery);
}

