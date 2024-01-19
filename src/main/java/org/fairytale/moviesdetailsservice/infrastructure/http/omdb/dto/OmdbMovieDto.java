package org.fairytale.moviesdetailsservice.infrastructure.http.omdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for OMDB movie.
 */
public record OmdbMovieDto(
    @JsonProperty("Title") String title,
    @JsonProperty("Awards") String awards
) {

}
