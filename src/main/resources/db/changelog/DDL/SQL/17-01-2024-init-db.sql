CREATE TABLE movies
(
    id          CHAR(36) PRIMARY KEY NOT NULL,
    title       VARCHAR(255),
    released_at DATE
);

CREATE INDEX idx_title ON movies (title);
CREATE INDEX idx_released_at ON movies (released_at);

CREATE TABLE nominations
(
    id    CHAR(36) PRIMARY KEY NOT NULL,
    title VARCHAR(255),
    date  DATE
);

CREATE INDEX idx_title ON nominations (title);
CREATE INDEX idx_date ON nominations (date);

CREATE TABLE nominees
(
    id            CHAR(36) PRIMARY KEY NOT NULL,
    movie_id      CHAR(36),
    nomination_id CHAR(36),
    is_winner     BOOLEAN,
    FOREIGN KEY (movie_id) REFERENCES movies (id),
    FOREIGN KEY (nomination_id) REFERENCES nominations (id)
);

CREATE INDEX idx_movie_id ON nominees (movie_id);
CREATE INDEX idx_nomination_id ON nominees (nomination_id);

CREATE TABLE ratings
(
    id       CHAR(36) PRIMARY KEY NOT NULL,
    movie_id CHAR(36),
    rating   SMALLINT,
    FOREIGN KEY (movie_id) REFERENCES movies (id)
);

CREATE INDEX idx_movie_id ON ratings (movie_id);
CREATE INDEX idx_rating ON ratings (rating);
