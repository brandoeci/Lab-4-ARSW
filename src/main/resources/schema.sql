CREATE TABLE IF NOT EXISTS blueprints (
                                          id     BIGSERIAL PRIMARY KEY,
                                          author VARCHAR(150) NOT NULL,
    name   VARCHAR(150) NOT NULL,
    CONSTRAINT uq_blueprint_author_name UNIQUE (author, name)
    );

CREATE TABLE IF NOT EXISTS points (
                                      id           BIGSERIAL PRIMARY KEY,
                                      blueprint_id BIGINT NOT NULL REFERENCES blueprints(id) ON DELETE CASCADE,
    x            INT NOT NULL,
    y            INT NOT NULL,
    seq          INT NOT NULL
    );