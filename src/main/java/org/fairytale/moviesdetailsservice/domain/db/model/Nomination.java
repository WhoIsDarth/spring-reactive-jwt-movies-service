package org.fairytale.moviesdetailsservice.domain.db.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Nomination domain model.
 */
public record Nomination(UUID id, LocalDate date, String title) {

}
