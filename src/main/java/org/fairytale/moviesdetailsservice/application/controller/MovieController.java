package org.fairytale.moviesdetailsservice.application.controller;

import java.util.List;
import java.util.UUID;
import org.fairytale.moviesdetailsservice.application.dto.CreateMovieRatingDto;
import org.fairytale.moviesdetailsservice.application.dto.MovieDto;
import org.fairytale.moviesdetailsservice.application.dto.MovieNominationStatusDto;
import org.fairytale.moviesdetailsservice.application.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controller for the movie resource.
 */
@RestController
@RequestMapping("/movies")
public class MovieController {

  private final MovieService movieService;

  /**
   * Constructor.
   *
   * @param movieService the movie service
   */
  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  // TODO: Better to return paginated response here, copy-paste from OMDB API

  /**
   * Finds a movie by query.
   *
   * @param title the title of the movie to find
   * @return the movie
   */
  @GetMapping("")
  public Mono<MovieDto> findByQuery(final @RequestParam("title") String title) {
    return movieService.findByTitle(title);
  }

  /**
   * Finds a movie by its id.
   *
   * @param id the id of the movie to find
   * @return the movie
   */
  @GetMapping("/{id}")
  public Mono<MovieDto> findById(final @PathVariable UUID id) {
    return movieService.findById(id);
  }

  // TODO: Better to use pagination here

  /**
   * Finds the top-rated movies.
   *
   * @param limit the limit of movies to find
   * @return the list of movies
   */
  @GetMapping("/top")
  public Mono<List<MovieDto>> findTopRatedMovies(final @RequestParam("limit") Integer limit) {
    return movieService.findTopRatedMovies(limit);
  }

  /**
   * Adds a rating to a movie.
   *
   * @param createMovieRatingDto the movie rating to add
   * @return a void mono
   */
  @PostMapping("/ratings")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Void> addMovieRating(final @RequestBody CreateMovieRatingDto createMovieRatingDto) {
    return movieService.addMovieRating(createMovieRatingDto.movieId(),
        createMovieRatingDto.stars());
  }

  /**
   * Finds the status of a movie nomination.
   *
   * @param movieTitle      the title of the movie
   * @param nominationTitle the title of the nomination
   * @return the status of the movie nomination
   */
  @GetMapping("/nominations/status")
  public Mono<MovieNominationStatusDto> findMovieNominationStatus(
      final @RequestParam("movie_title") String movieTitle,
      final @RequestParam("nomination_title") String nominationTitle) {
    return movieService.findMovieNominationStatus(movieTitle, nominationTitle);
  }
}
