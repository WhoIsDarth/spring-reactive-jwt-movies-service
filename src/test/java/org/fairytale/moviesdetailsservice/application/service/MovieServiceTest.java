package org.fairytale.moviesdetailsservice.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import org.fairytale.moviesdetailsservice.application.dto.MovieDto;
import org.fairytale.moviesdetailsservice.application.dto.MovieNominationStatusDto;
import org.fairytale.moviesdetailsservice.common.exception.ResourceNotFoundException;
import org.fairytale.moviesdetailsservice.domain.clients.OmdbClient;
import org.fairytale.moviesdetailsservice.domain.db.model.Movie;
import org.fairytale.moviesdetailsservice.domain.db.repository.MovieRepository;
import org.fairytale.moviesdetailsservice.domain.db.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class MovieServiceTest {

  private MovieRepository movieRepository;
  private RatingRepository ratingRepository;
  private OmdbClient omdbClient;
  private MovieService movieService;

  @BeforeEach
  void setUp() {
    movieRepository = Mockito.mock(MovieRepository.class);
    ratingRepository = Mockito.mock(RatingRepository.class);
    omdbClient = Mockito.mock(OmdbClient.class);
    movieService = new MovieService(movieRepository, ratingRepository, omdbClient);
  }

  @Test
  @DisplayName("Should return movies when movies exist")
  void findById_ShouldReturnMovie_WhenMovieExists() {
    UUID movieId = UUID.randomUUID();
    Movie movie = new Movie(movieId, "Test Movie", LocalDate.now());
    when(movieRepository.findById(movieId)).thenReturn(Mono.just(movie));

    Mono<MovieDto> result = movieService.findById(movieId);

    result.subscribe(movieDto -> {
      assert movieDto.id().equals(movieId);
      assert movieDto.title().equals("Test Movie");
    });
  }

  @Test
  @DisplayName("Should throw exception when movie not found")
  void findById_ShouldThrowException_WhenMovieNotFound() {
    UUID movieId = UUID.randomUUID();
    when(movieRepository.findById(movieId)).thenReturn(
        Mono.error(new ResourceNotFoundException("Movie not found")));

    Mono<MovieDto> result = movieService.findById(movieId);

    assertThrows(ResourceNotFoundException.class, result::block);
  }

  @Test
  @DisplayName("Should return movies when movies exist")
  void findTopRatedMovies_ShouldReturnMovies_WhenMoviesExist() {
    int limit = 5;
    List<Movie> movies = IntStream.range(0, limit)
        .mapToObj(i -> new Movie(UUID.randomUUID(), "Movie " + i, LocalDate.now())).toList();
    when(movieRepository.findTopRatedMovies(limit)).thenReturn(Mono.just(movies));

    Mono<List<MovieDto>> result = movieService.findTopRatedMovies(limit);

    result.subscribe(movieDtos -> {
      assertEquals(limit, movieDtos.size());
      for (int i = 0; i < limit; i++) {
        assertEquals("Movie " + i, movieDtos.get(i).title());
      }
    });
  }

  @Test
  @DisplayName("Should return movie nomination when movies exist")
  void findMovieNominationStatus_ShouldReturnStatus_WhenMovieAndNominationExist() {
    String movieTitle = "Test Movie";
    String nominationTitle = "Best Picture";

    when(movieRepository.isMovieWonNomination(movieTitle, nominationTitle))
        .thenReturn(Mono.empty());

    when(omdbClient.isMovieWonAward(movieTitle, nominationTitle))
        .thenReturn(Mono.just(Boolean.TRUE));

    Mono<MovieNominationStatusDto> result = movieService.findMovieNominationStatus(movieTitle,
        nominationTitle);

    StepVerifier.create(result)
        .assertNext(status -> assertTrue(status.isWinner()))
        .verifyComplete();
  }

  @Test
  @DisplayName("Should complete when movies exist")
  void addMovieRating_ShouldComplete_WhenMovieExists() {
    UUID movieId = UUID.randomUUID();
    short stars = 4;
    when(movieRepository.findById(movieId)).thenReturn(
        Mono.just(new Movie(movieId, "Test Movie", LocalDate.now())));
    when(ratingRepository.create(movieId, stars)).thenReturn(Mono.empty());

    Mono<Void> result = movieService.addMovieRating(movieId, stars);

    StepVerifier.create(result).verifyComplete();
  }

  @Test
  @DisplayName("Should return movie when movie with given title exists")
  void findByTitle_ShouldReturnMovie_WhenMovieExists() {
    String movieTitle = "Test Movie";
    Movie movie = new Movie(UUID.randomUUID(), movieTitle, LocalDate.now());
    when(movieRepository.findByTitle(movieTitle)).thenReturn(Mono.just(movie));

    Mono<MovieDto> result = movieService.findByTitle(movieTitle);

    StepVerifier.create(result)
        .assertNext(movieDto -> {
          assertEquals(movieTitle, movieDto.title());
          assertNotNull(movieDto.id());
        })
        .verifyComplete();
  }

  @Test
  @DisplayName("Should throw exception when movie with given title not found")
  void findByTitle_ShouldThrowException_WhenMovieNotFound() {
    String movieTitle = "Unknown Movie";
    when(movieRepository.findByTitle(movieTitle)).thenReturn(Mono.empty());

    Mono<MovieDto> result = movieService.findByTitle(movieTitle);

    StepVerifier.create(result)
        .expectError(ResourceNotFoundException.class)
        .verify();
  }

  @Test
  @DisplayName("Should return movie nomination status from OMDB client when not found in local repository")
  void findMovieNominationStatus_ShouldReturnStatusFromOmdb_WhenNotInLocalRepo() {
    String movieTitle = "Test Movie";
    String nominationTitle = "Best Picture";
    when(movieRepository.isMovieWonNomination(movieTitle, nominationTitle)).thenReturn(
        Mono.empty());
    when(omdbClient.isMovieWonAward(movieTitle, nominationTitle)).thenReturn(
        Mono.just(Boolean.TRUE));

    Mono<MovieNominationStatusDto> result = movieService.findMovieNominationStatus(movieTitle,
        nominationTitle);

    StepVerifier.create(result)
        .assertNext(status -> assertTrue(status.isWinner()))
        .verifyComplete();
  }

  @Test
  @DisplayName("Should throw exception when adding rating to non-existent movie")
  void addMovieRating_ShouldThrowException_WhenMovieNotFound() {
    UUID movieId = UUID.randomUUID();
    short stars = 4;
    when(movieRepository.findById(movieId)).thenReturn(Mono.empty());

    Mono<Void> result = movieService.addMovieRating(movieId, stars);

    StepVerifier.create(result)
        .expectError(ResourceNotFoundException.class)
        .verify();
  }
}