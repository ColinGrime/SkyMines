CREATE TABLE IF NOT EXISTS skymines_mines (
    uuid      VARCHAR(36) NOT NULL,
    owner     VARCHAR(36) NOT NULL,
    structure TEXT        NOT NULL,
    upgrades  TEXT        NOT NULL,
    home      TEXT        NOT NULL,
    PRIMARY KEY (uuid)
);