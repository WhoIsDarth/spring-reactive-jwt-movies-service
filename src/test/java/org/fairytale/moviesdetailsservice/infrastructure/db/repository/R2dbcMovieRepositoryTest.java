package org.fairytale.moviesdetailsservice.infrastructure.db.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import org.fairytale.moviesdetailsservice.domain.db.model.Movie;
import org.fairytale.moviesdetailsservice.infrastructure.db.entity.MovieTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.FetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class R2dbcMovieRepositoryTest {

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

  private R2dbcEntityTemplate template;
  private R2dbcMovieRepository repository;

  @BeforeEach
  void setUp() {
    template = Mockito.mock(R2dbcEntityTemplate.class);
    repository = new R2dbcMovieRepository(template);
  }

  @Test
  void findById_ShouldReturnMovie_WhenMovieExists() {
    UUID movieId = UUID.randomUUID();
    MovieTable movieTable = new MovieTable(movieId.toString(), "Test Movie", LocalDate.now());
    when(template.selectOne(any(Query.class), Mockito.eq(MovieTable.class))).thenReturn(
        Mono.just(movieTable));

    Mono<Movie> result = repository.findById(movieId);

    StepVerifier.create(result).assertNext(movie -> {
      assertEquals(movieId, movie.id());
      assertEquals("Test Movie", movie.title());
    }).verifyComplete();
  }

  @Test
  void findById_ShouldReturnEmpty_WhenMovieNotFound() {
    UUID movieId = UUID.randomUUID();
    when(template.selectOne(any(Query.class), Mockito.eq(MovieTable.class))).thenReturn(
        Mono.empty());

    Mono<Movie> result = repository.findById(movieId);

    StepVerifier.create(result).verifyComplete();
  }

  @Test
  void findByTitle_ShouldReturnMovie_WhenMovieExists() {
    String movieTitle = "Test Movie";
    MovieTable movieTable = new MovieTable(UUID.randomUUID().toString(), movieTitle,
        LocalDate.now());

    DatabaseClient.GenericExecuteSpec executeSpec = Mockito.mock(
        DatabaseClient.GenericExecuteSpec.class);
    FetchSpec<MovieTable> fetchSpec = Mockito.mock(FetchSpec.class);

    when(template.getDatabaseClient()).thenReturn(Mockito.mock(DatabaseClient.class));
    when(template.getDatabaseClient().sql(SELECT_MOVIE_BY_TITLE_QUERY)).thenReturn(executeSpec);
    when(executeSpec.bind("title", "%" + movieTitle + "%")).thenReturn(executeSpec);
    when(executeSpec.map(any(BiFunction.class))).thenReturn(fetchSpec);
    when(fetchSpec.first()).thenReturn(Mono.just(movieTable));

    Mono<Movie> result = repository.findByTitle(movieTitle);

    StepVerifier.create(result).assertNext(movie -> assertEquals(movieTitle, movie.title()))
        .verifyComplete();
  }

  @Test
  void findByTitle_ShouldReturnEmpty_WhenMovieNotFound() {
    String movieTitle = "Nonexistent Movie";

    DatabaseClient.GenericExecuteSpec executeSpec = Mockito.mock(
        DatabaseClient.GenericExecuteSpec.class);
    FetchSpec<MovieTable> fetchSpec = Mockito.mock(FetchSpec.class);

    when(template.getDatabaseClient()).thenReturn(Mockito.mock(DatabaseClient.class));
    when(template.getDatabaseClient().sql(SELECT_MOVIE_BY_TITLE_QUERY)).thenReturn(executeSpec);
    when(executeSpec.bind("title", "%" + movieTitle + "%")).thenReturn(executeSpec);
    when(executeSpec.map(any(BiFunction.class))).thenReturn(fetchSpec);
    when(fetchSpec.first()).thenReturn(Mono.empty());

    Mono<Movie> result = repository.findByTitle(movieTitle);

    StepVerifier.create(result)
        .verifyComplete();
  }

  @Test
  void findTopRatedMovies_ShouldReturnMovies_WhenMoviesExist() {
    int limit = 3;
    List<MovieTable> movieTables = List.of(
        new MovieTable(UUID.randomUUID().toString(), "Movie 1", LocalDate.now()),
        new MovieTable(UUID.randomUUID().toString(), "Movie 2", LocalDate.now()),
        new MovieTable(UUID.randomUUID().toString(), "Movie 3", LocalDate.now()));

    DatabaseClient.GenericExecuteSpec executeSpec = Mockito.mock(
        DatabaseClient.GenericExecuteSpec.class);
    FetchSpec<MovieTable> fetchSpec = Mockito.mock(FetchSpec.class);

    when(template.getDatabaseClient()).thenReturn(Mockito.mock(DatabaseClient.class));
    when(template.getDatabaseClient().sql(SELECT_TOP_RATED_MOVIES_QUERY)).thenReturn(executeSpec);
    when(executeSpec.bind("limit", limit)).thenReturn(executeSpec);
    when(executeSpec.bind(anyString(), any())).thenReturn(executeSpec);

    // Adjusted mocking for map method
    when(fetchSpec.all()).thenReturn(Flux.fromIterable(movieTables));
    when(executeSpec.map(any(BiFunction.class))).thenReturn(fetchSpec);

    Mono<List<Movie>> result = repository.findTopRatedMovies(limit);

    StepVerifier.create(result).assertNext(movies -> assertEquals(limit, movies.size()))
        .verifyComplete();
  }
}
