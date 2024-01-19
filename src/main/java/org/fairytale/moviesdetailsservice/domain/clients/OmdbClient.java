package org.fairytale.moviesdetailsservice.domain.clients;

import reactor.core.publisher.Mono;

/**
 * Omdb client.
 */
public interface OmdbClient {

  /**
   * Check if a movie has won a specific award.
   *
   * @param title the title of the movie
   * @param award the award to check
   * @return Boolean true if the movie won award, false if not, empty if the movie is not found
   */
  Mono<Boolean> isMovieWonAward(final String title, final String award);
}
