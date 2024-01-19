package org.fairytale.moviesdetailsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestMoviesDetailsServiceApplication {

  public static void main(String[] args) {
    SpringApplication.from(MoviesDetailsServiceApplication::main)
        .with(TestMoviesDetailsServiceApplication.class).run(args);
  }

}
