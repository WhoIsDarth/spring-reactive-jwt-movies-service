package org.fairytale.moviesdetailsservice.domain.db.repository;

import java.util.UUID;
import reactor.core.publisher.Mono;

/**
 * Movie rating repository.
 */
public interface RatingRepository {

  /**
   * Create a rating for a movie.
   *
   * @param movieId the id of the movie
   * @param stars   the number of rating
   * @return the created rating
   */
  Mono<Void> create(final UUID movieId, final short stars);
}
