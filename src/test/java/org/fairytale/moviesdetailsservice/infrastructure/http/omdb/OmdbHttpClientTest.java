package org.fairytale.moviesdetailsservice.infrastructure.http.omdb;

import static org.mockito.Mockito.when;
import java.util.function.Function;
import org.fairytale.moviesdetailsservice.common.configuration.OmdbProperties;
import org.fairytale.moviesdetailsservice.infrastructure.http.omdb.dto.OmdbMovieDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class OmdbHttpClientTest {

  private OmdbHttpClient omdbHttpClient;
  private WebClient.ResponseSpec responseSpecMock;

  @BeforeEach
  void setUp() {
    OmdbProperties omdbProperties = new OmdbProperties();
    omdbProperties.setApiKey("testApiKey");

    WebClient webClientMock = Mockito.mock(WebClient.class);
    WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock = Mockito.mock(
        WebClient.RequestHeadersUriSpec.class);
    WebClient.RequestHeadersSpec requestHeadersSpecMock = Mockito.mock(
        WebClient.RequestHeadersSpec.class);
    responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);

    when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
    when(requestHeadersUriSpecMock.uri(Mockito.any(Function.class))).thenReturn(
        requestHeadersSpecMock);
    when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);

    omdbHttpClient = new OmdbHttpClient(omdbProperties, webClientMock);
  }

  @Test
  @DisplayName("Should return true when award is won")
  void isMovieWonAward_ShouldReturnTrue_WhenAwardIsWon() {
    OmdbMovieDto omdbMovieDto = new OmdbMovieDto("Movie Title", "Won Best Picture");
    when(responseSpecMock.bodyToMono(OmdbMovieDto.class)).thenReturn(Mono.just(omdbMovieDto));

    Mono<Boolean> result = omdbHttpClient.isMovieWonAward("Movie Title", "Best Picture");

    StepVerifier.create(result)
        .expectNext(true)
        .verifyComplete();
  }

  @Test
  @DisplayName("Should return false when award is not won")
  void isMovieWonAward_ShouldReturnFalse_WhenAwardIsNotWon() {
    OmdbMovieDto omdbMovieDto = new OmdbMovieDto("Movie Title", "Nominated for Best Picture");
    when(responseSpecMock.bodyToMono(OmdbMovieDto.class)).thenReturn(Mono.just(omdbMovieDto));

    Mono<Boolean> result = omdbHttpClient.isMovieWonAward("Movie Title", "Best Picture");

    StepVerifier.create(result)
        .expectNext(false)
        .verifyComplete();
  }
}
