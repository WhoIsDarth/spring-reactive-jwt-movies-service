package org.fairytale.moviesdetailsservice.domain.db.repository;

import java.util.List;
import java.util.UUID;
import org.fairytale.moviesdetailsservice.domain.db.model.Movie;
import reactor.core.publisher.Mono;

/**
 * Repository for movies.
 */
public interface MovieRepository {

  /**
   * Find a movie by its id.
   *
   * @param id the id of the movie
   * @return the movie
   */
  Mono<Movie> findById(final UUID id);

  /**
   * Find a movie by its title.
   *
   * @param title the title of the movie
   * @return the movie
   */
  Mono<Movie> findByTitle(final String title);

  /**
   * Check if a movie has won a nomination.
   *
   * @param movieTitle      the title of the movie
   * @param nominationTitle the title of the nomination
   * @return Boolean true if the movie won, false if not, empty if the movie is not found
   */
  Mono<Boolean> isMovieWonNomination(final String movieTitle, final String nominationTitle);

  /**
   * Find top-rated movies.
   *
   * @param limit the maximum number of movies to return
   * @return the list of top-rated movies
   */
  Mono<List<Movie>> findTopRatedMovies(final Integer limit);
}
