package com.ramusthastudio.ama;

import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder app) {
    return app.sources(Application.class);
  }

  public static void main(String[] args) throws NoSuchAlgorithmException {
    // SSLContext.getInstance("TLS");
    SSLContext.getInstance("SSL_TLSv2");
    SpringApplication.run(Application.class, args);
  }
}
