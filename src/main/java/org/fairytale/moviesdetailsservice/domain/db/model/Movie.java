package org.fairytale.moviesdetailsservice.domain.db.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Movie domain model.
 */
public record Movie(UUID id, String title, LocalDate releasedAt) {

}
