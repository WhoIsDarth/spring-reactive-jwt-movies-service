package org.fairytale.moviesdetailsservice.infrastructure.http.omdb;

import org.fairytale.moviesdetailsservice.common.configuration.OmdbProperties;
import org.fairytale.moviesdetailsservice.domain.clients.OmdbClient;
import org.fairytale.moviesdetailsservice.infrastructure.http.omdb.dto.OmdbMovieDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * HTTP implementation of {@link OmdbClient} client.
 */
@Component
public class OmdbHttpClient implements OmdbClient {

  private final WebClient webClient;
  private final String apiKey;

  /**
   * Constructor.
   *
   * @param omdbProperties OMDB properties.
   * @param webClient      Web client.
   */
  public OmdbHttpClient(final OmdbProperties omdbProperties,
      @Qualifier("omdbWebClient") final WebClient webClient) {
    this.webClient = webClient;
    this.apiKey = omdbProperties.getApiKey();
  }

  @Override
  public Mono<Boolean> isMovieWonAward(final String title, final String award) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.queryParam("t", title).queryParam("apikey", apiKey).build())
        .retrieve()
        .bodyToMono(OmdbMovieDto.class)
        .map(omdbMovieDto -> {
          String awardsText = omdbMovieDto.awards();
          return awardsText != null && containsAward(awardsText, award);
        });
  }

  private boolean containsAward(String awardsText, String award) {
    String lowerCaseAwards = awardsText.toLowerCase();
    return lowerCaseAwards.contains("won") && lowerCaseAwards.contains(award.toLowerCase());
  }
}

