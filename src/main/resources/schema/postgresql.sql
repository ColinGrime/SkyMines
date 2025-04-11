CREATE TABLE IF NOT EXISTS skymines_mines (
    uuid       UUID  NOT NULL PRIMARY KEY,
    owner      UUID  NOT NULL,
    identifier TEXT  NOT NULL,
    structure  JSONB NOT NULL,
    upgrades   JSONB NOT NULL,
    home       JSONB NOT NULL
);