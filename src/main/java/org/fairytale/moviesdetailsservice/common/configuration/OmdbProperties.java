package org.fairytale.moviesdetailsservice.common.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * OMDB properties.
 */
@Configuration
@ConfigurationProperties(prefix = "omdb")
public class OmdbProperties {

  private String apiKey;
  private String baseUrl;

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }
}
