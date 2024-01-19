package org.fairytale.moviesdetailsservice.domain.db.model;

/**
 * Rating domain model.
 */
public record Rating(String id, Movie movie, short rating) {

}
