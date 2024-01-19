package org.fairytale.moviesdetailsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Movies details service application.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class MoviesDetailsServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(MoviesDetailsServiceApplication.class, args);
  }

}
