package org.fairytale.moviesdetailsservice.application.controller;

import static org.mockito.ArgumentMatchers.eq;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.fairytale.moviesdetailsservice.application.dto.CreateMovieRatingDto;
import org.fairytale.moviesdetailsservice.application.dto.MovieDto;
import org.fairytale.moviesdetailsservice.application.dto.MovieNominationStatusDto;
import org.fairytale.moviesdetailsservice.application.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


class MovieControllerTest {

  private WebTestClient webTestClient;
  private MovieService movieService;

  @BeforeEach
  void setUp() {
    movieService = Mockito.mock(MovieService.class);
    MovieController movieController = new MovieController(movieService);
    webTestClient = WebTestClient.bindToController(movieController).build();
  }

  @Test
  @DisplayName("Should return movie when movie with given id exists")
  void findByIdTest() {
    UUID movieId = UUID.randomUUID();
    MovieDto movieDto = new MovieDto(movieId, "Test Movie");
    Mockito.when(movieService.findById(eq(movieId))).thenReturn(Mono.just(movieDto));

    webTestClient.get().uri("/movies/{id}", movieId).exchange().expectStatus().isOk()
        .expectBody(MovieDto.class).isEqualTo(movieDto);
  }

  @Test
  @DisplayName("Should return movies when movies exist")
  void findTopRatedMoviesTest() {
    List<MovieDto> movies = Arrays.asList(new MovieDto(UUID.randomUUID(), "Movie1"),
        new MovieDto(UUID.randomUUID(), "Movie2"));
    Mockito.when(movieService.findTopRatedMovies(eq(2))).thenReturn(Mono.just(movies));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/movies/top").queryParam("limit", 2).build()).exchange()
        .expectStatus().isOk().expectBodyList(MovieDto.class).isEqualTo(movies);
  }

  @Test
  @DisplayName("Should return movie rating when movie with given id exists")
  void addMovieRatingTest() {
    CreateMovieRatingDto createMovieRatingDto = new CreateMovieRatingDto(UUID.randomUUID(),
        (short) 4);
    Mockito.when(movieService.addMovieRating(eq(createMovieRatingDto.movieId()),
        eq(createMovieRatingDto.stars()))).thenReturn(Mono.empty());

    webTestClient.post().uri("/movies/ratings").contentType(MediaType.APPLICATION_JSON)
        .bodyValue(createMovieRatingDto).exchange().expectStatus().isCreated();
  }

  @Test
  @DisplayName("Should return movie nomination status when movie with given title exists")
  void findMovieNominationStatusTest() {
    MovieNominationStatusDto nominationStatus = new MovieNominationStatusDto(true);
    Mockito.when(movieService.findMovieNominationStatus(eq("Movie Title"), eq("Nomination Title")))
        .thenReturn(Mono.just(nominationStatus));

    webTestClient.get().uri(uriBuilder -> uriBuilder.path("/movies/nominations/status")
            .queryParam("movie_title", "Movie Title").queryParam("nomination_title", "Nomination Title")
            .build()).exchange().expectStatus().isOk().expectBody(MovieNominationStatusDto.class)
        .isEqualTo(nominationStatus);
  }

  @Test
  @DisplayName("Should return movie when movie with given title exists")
  void findByQueryTest() {
    String movieTitle = "Test Movie";
    MovieDto movieDto = new MovieDto(UUID.randomUUID(), movieTitle);
    Mockito.when(movieService.findByTitle(eq(movieTitle))).thenReturn(Mono.just(movieDto));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/movies").queryParam("title", movieTitle).build())
        .exchange().expectStatus().isOk().expectBody(MovieDto.class).isEqualTo(movieDto);
  }
}
