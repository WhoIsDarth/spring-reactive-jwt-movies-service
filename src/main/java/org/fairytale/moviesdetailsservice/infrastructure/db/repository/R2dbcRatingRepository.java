package org.fairytale.moviesdetailsservice.infrastructure.db.repository;

import java.util.UUID;
import org.fairytale.moviesdetailsservice.domain.db.repository.RatingRepository;
import org.fairytale.moviesdetailsservice.infrastructure.db.entity.RatingTable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * R2DBC implementation of the {@link RatingRepository}.
 */
@Repository
public class R2dbcRatingRepository implements RatingRepository {

  private final R2dbcEntityTemplate template;

  /**
   * Constructor.
   *
   * @param entityTemplate the entity template
   */
  public R2dbcRatingRepository(final R2dbcEntityTemplate entityTemplate) {
    this.template = entityTemplate;
  }

  @Override
  public Mono<Void> create(final UUID movieId, final short stars) {
    return template.insert(new RatingTable(UUID.randomUUID().toString(), movieId.toString(), stars))
        .then();
  }
}
