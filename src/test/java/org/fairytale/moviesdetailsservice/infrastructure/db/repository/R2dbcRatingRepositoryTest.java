package org.fairytale.moviesdetailsservice.infrastructure.db.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.UUID;
import org.fairytale.moviesdetailsservice.infrastructure.db.entity.RatingTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class R2dbcRatingRepositoryTest {

  private R2dbcEntityTemplate template;
  private R2dbcRatingRepository repository;

  @BeforeEach
  void setUp() {
    template = Mockito.mock(R2dbcEntityTemplate.class);
    repository = new R2dbcRatingRepository(template);
  }

  @Test
  void create_ShouldInsertRating_WhenCalled() {
    UUID movieId = UUID.randomUUID();
    short stars = 5;
    when(template.insert(any(RatingTable.class))).thenReturn(Mono.empty());

    Mono<Void> result = repository.create(movieId, stars);

    StepVerifier.create(result).verifyComplete();

    Mockito.verify(template).insert(any(RatingTable.class));
  }
}
