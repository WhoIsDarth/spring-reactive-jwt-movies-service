package org.fairytale.moviesdetailsservice.infrastructure.db.entity;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Movie table.
 */
@Table("movies")
public record MovieTable(
    @Id @Column("id") String id,
    @Column("title") String title,
    @Column("released_at") LocalDate releasedAt
) {

}
