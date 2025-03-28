CREATE TABLE IF NOT EXISTS skymines_mines (
    uuid      UUID NOT NULL PRIMARY KEY,
    owner     UUID NOT NULL,
    structure TEXT NOT NULL,
    upgrades  TEXT NOT NULL,
    home      TEXT NOT NULL
);