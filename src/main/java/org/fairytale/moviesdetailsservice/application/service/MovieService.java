package org.fairytale.moviesdetailsservice.application.service;

import java.util.List;
import java.util.UUID;
import org.fairytale.moviesdetailsservice.application.dto.MovieDto;
import org.fairytale.moviesdetailsservice.application.dto.MovieNominationStatusDto;
import org.fairytale.moviesdetailsservice.common.exception.ResourceNotFoundException;
import org.fairytale.moviesdetailsservice.domain.clients.OmdbClient;
import org.fairytale.moviesdetailsservice.domain.db.repository.MovieRepository;
import org.fairytale.moviesdetailsservice.domain.db.repository.RatingRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service for movies.
 */
@Service
public class MovieService {

  private final MovieRepository movieRepository;
  private final RatingRepository ratingRepository;
  private final OmdbClient omdbClient;

  /**
   * Constructor.
   *
   * @param movieRepository  the movie repository
   * @param ratingRepository the rating repository
   * @param omdbClient       the OMDB client
   */
  public MovieService(final MovieRepository movieRepository,
      final RatingRepository ratingRepository, final OmdbClient omdbClient) {
    this.movieRepository = movieRepository;
    this.ratingRepository = ratingRepository;
    this.omdbClient = omdbClient;
  }

  /**
   * Find a movie by its id.
   *
   * @param id the id of the movie
   * @return the movie
   * @throws ResourceNotFoundException if the movie is not found
   */
  public Mono<MovieDto> findById(final UUID id) {
    return movieRepository.findById(id)
        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Movie not found.")))
        .map(movie -> new MovieDto(movie.id(), movie.title()));
  }

  /**
   * Find a movie by its title.
   *
   * @param title the title of the movie
   * @return the movie
   * @throws ResourceNotFoundException if the movie is not found
   */
  public Mono<MovieDto> findByTitle(final String title) {
    return movieRepository.findByTitle(title)
        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Movie not found.")))
        .map(movie -> new MovieDto(movie.id(), movie.title()));
  }

  /**
   * Find the top-rated movies.
   *
   * @param limit the number of movies to return
   * @return the movies
   */
  public Mono<List<MovieDto>> findTopRatedMovies(final Integer limit) {
    return movieRepository.findTopRatedMovies(limit).map(
        movies -> movies.stream().map(movie -> new MovieDto(movie.id(), movie.title())).toList());
  }

  /**
   * Find the top-rated movies.
   *
   * @param movieTitle      the title of the movie
   * @param nominationTitle the title of the nomination
   * @return the movie nomination status
   */
  public Mono<MovieNominationStatusDto> findMovieNominationStatus(final String movieTitle,
      final String nominationTitle) {
    return movieRepository.isMovieWonNomination(movieTitle, nominationTitle)
        .flatMap(won -> Mono.just(new MovieNominationStatusDto(won))).switchIfEmpty(
            omdbClient.isMovieWonAward(movieTitle, nominationTitle)
                // TODO: Can be saved in the database for future use
                .flatMap(won -> Mono.just(new MovieNominationStatusDto(won))).switchIfEmpty(
                    Mono.error(new ResourceNotFoundException("Movie or nomination not found."))));
  }

  /**
   * Rate a movie.
   *
   * @param movieId the id of the movie
   * @param stars   the number of rating
   * @return the movie
   * @throws ResourceNotFoundException if the movie is not found
   */
  public Mono<Void> addMovieRating(final UUID movieId, final short stars) {
    return movieRepository.findById(movieId)
        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Movie not found.")))
        .flatMap(movie -> ratingRepository.create(movieId, stars));
  }
}
