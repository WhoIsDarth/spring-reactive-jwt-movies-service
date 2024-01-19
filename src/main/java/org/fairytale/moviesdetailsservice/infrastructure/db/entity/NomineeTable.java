package org.fairytale.moviesdetailsservice.infrastructure.db.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Nominee table.
 */
@Table("nominees")
public record NomineeTable(
    @Id @Column("id") String id,
    @Column("movie_id") String movieId,
    @Column("nomination_id") String nominationId,
    @Column("is_winner") short isWinner
) {

}
