package org.fairytale.moviesdetailsservice.infrastructure.db.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Rating table.
 */
@Table("ratings")
public record RatingTable(@Id @Column("id") String id, @Column("movie_id") String movieId,
                          @Column("rating") short rating) {

}
