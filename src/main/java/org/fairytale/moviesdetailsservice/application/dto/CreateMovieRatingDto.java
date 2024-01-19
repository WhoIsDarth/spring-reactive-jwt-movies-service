package org.fairytale.moviesdetailsservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.UUID;

/**
 * DTO for creating a movie rating.
 */
public record CreateMovieRatingDto(@JsonProperty("movie_id") UUID movieId,
                                   @Min(0) @Max(5) short stars) {

}
