package com.ramusthastudio.ama.util;

import com.ramusthastudio.ama.database.Dao;
import com.ramusthastudio.ama.database.DaoImpl;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class Config {
  @Autowired
  Environment mEnv;

  @Bean
  public DataSource getDataSource() {
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

  @Bean
  public Dao getPersonDao() { return new DaoImpl(getDataSource()); }

  @Bean(name = "line.bot.channelSecret")
  public String getChannelSecret() {
    return mEnv.getProperty("line.bot.channelSecret");
  }

  @Bean(name = "line.bot.channelToken")
  public String getChannelAccessToken() {
    return mEnv.getProperty("line.bot.channelToken");
  }
}
