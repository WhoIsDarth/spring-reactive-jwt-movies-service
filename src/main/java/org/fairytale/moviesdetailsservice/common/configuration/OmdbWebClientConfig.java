package org.fairytale.moviesdetailsservice.common.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OmdbWebClientConfig {

  @Bean
  @Qualifier("omdbWebClient")
  public WebClient webClient(OmdbProperties omdbProperties) {
    return WebClient.builder()
        .baseUrl(omdbProperties.getBaseUrl())
        .build();
  }
}
