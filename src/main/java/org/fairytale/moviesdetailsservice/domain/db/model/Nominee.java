package org.fairytale.moviesdetailsservice.domain.db.model;

import java.util.UUID;

/**
 * Nominee domain model.
 */
public record Nominee(UUID id, UUID movieId, UUID nominationId, boolean isWinner) {

}
