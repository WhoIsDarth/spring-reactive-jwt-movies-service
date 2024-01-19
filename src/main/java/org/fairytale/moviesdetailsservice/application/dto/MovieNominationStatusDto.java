package org.fairytale.moviesdetailsservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for movie nomination status.
 */
public record MovieNominationStatusDto(@JsonProperty("is_winner") boolean isWinner) {

}
