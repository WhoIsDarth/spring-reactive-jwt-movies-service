package org.fairytale.moviesdetailsservice.infrastructure.db.repository;

import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.fairytale.moviesdetailsservice.domain.db.model.Movie;
import org.fairytale.moviesdetailsservice.domain.db.repository.MovieRepository;
import org.fairytale.moviesdetailsservice.infrastructure.db.entity.MovieTable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * R2DBC implementation of the {@link MovieRepository}.
 */
@Repository
public class R2dbcMovieRepository implements MovieRepository {

  private static final String SELECT_TOP_RATED_MOVIES_QUERY = """
      SELECT m.id, m.title, m.released_at
      FROM movies m
      JOIN ratings r ON m.id = r.movie_id
      GROUP BY m.id, m.title, m.released_at
      ORDER BY AVG(r.rating) DESC
      LIMIT :limit
      """;
  private static final String SELECT_MOVIE_BY_TITLE_QUERY = """
      SELECT id, title, released_at
      FROM movies
      WHERE LOWER(title) LIKE LOWER(:title)
      LIMIT 1
      """;
  private static final String CHECK_MOVIE_EXISTS_BY_TITLE_QUERY = """
      SELECT COUNT(*)
      FROM movies
      WHERE title = :movieTitle
      """;

  private static final String CHECK_MOVIE_WON_NOMINATION_QUERY = """
      SELECT COUNT(*)
      FROM nominees no
      JOIN movies m ON no.movie_id = m.id
      JOIN nominations n ON no.nomination_id = n.id
      WHERE m.title = :movieTitle
      AND n.title = :nominationTitle
      AND no.is_winner = TRUE
      """;

  private final R2dbcEntityTemplate template;

  /**
   * Constructor.
   *
   * @param entityTemplate the entity template
   */
  public R2dbcMovieRepository(final R2dbcEntityTemplate entityTemplate) {
    this.template = entityTemplate;
  }

  @Override
  public Mono<Movie> findById(final UUID id) {
    return template.selectOne(Query.query(Criteria.where("id").is(id)), MovieTable.class)
        .map(this::toDomainModel);
  }

  @Override
  public Mono<Movie> findByTitle(final String title) {
    return template.getDatabaseClient().sql(SELECT_MOVIE_BY_TITLE_QUERY)
        .bind("title", String.format("%%%s%%", title)).map((row, metadata) -> toMovieTable(row))
        .first().map(this::toDomainModel);
  }

  @Override
  public Mono<Boolean> isMovieWonNomination(final String movieTitle, final String nominationTitle) {
    return template.getDatabaseClient().sql(CHECK_MOVIE_EXISTS_BY_TITLE_QUERY)
        .bind("movieTitle", movieTitle)
        .map((row, metadata) -> Optional.ofNullable(row.get(0, Integer.class))).first()
        .flatMap(countOpt -> {
          if (countOpt.orElse(0) == 0) {
            return Mono.empty();
          }
          return template.getDatabaseClient().sql(CHECK_MOVIE_WON_NOMINATION_QUERY)
              .bind("movieTitle", movieTitle).bind("nominationTitle", nominationTitle)
              .map((row, metadata) -> Optional.ofNullable(row.get(0, Integer.class)).orElse(0) > 0)
              .first().defaultIfEmpty(false);
        });
  }


  @Override
  public Mono<List<Movie>> findTopRatedMovies(final Integer limit) {
    return template.getDatabaseClient().sql(SELECT_TOP_RATED_MOVIES_QUERY).bind("limit", limit)
        .map((row, metadata) -> toMovieTable(row)).all().collectList()
        .map(movieTables -> movieTables.stream().map(this::toDomainModel).toList());
  }

  private MovieTable toMovieTable(final Row row) {
    return new MovieTable(row.get("id", String.class), row.get("title", String.class),
        row.get("released_at", LocalDate.class));
  }

  private Movie toDomainModel(final MovieTable movieTable) {
    return new Movie(UUID.fromString(movieTable.id()), movieTable.title(), movieTable.releasedAt());
  }
}
