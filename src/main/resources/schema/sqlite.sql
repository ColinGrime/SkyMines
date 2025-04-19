CREATE TABLE IF NOT EXISTS skymines_mines_v2 (
    uuid       VARCHAR(36) NOT NULL PRIMARY KEY,
    owner      VARCHAR(36) NOT NULL,
    identifier TEXT        NOT NULL,
    structure  TEXT        NOT NULL,
    upgrades   TEXT        NOT NULL,
    home       TEXT        NOT NULL
);