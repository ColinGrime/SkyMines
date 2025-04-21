CREATE TABLE IF NOT EXISTS skymines_mines_v2 (
    uuid       VARCHAR(36) NOT NULL,
    owner      VARCHAR(36) NOT NULL,
    identifier TEXT        NOT NULL,
    structure  JSON        NOT NULL,
    upgrades   JSON        NOT NULL,
    home       JSON        NOT NULL,
    name       TEXT        NULL,
    PRIMARY KEY (uuid)
);