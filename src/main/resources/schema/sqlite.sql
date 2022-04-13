CREATE TABLE IF NOT EXISTS `skymines_mines` (
    `uuid`      VARCHAR(36)  NOT NULL PRIMARY KEY,
    `owner`     VARCHAR(36)  NOT NULL,
    `structure` VARCHAR(200) NOT NULL,
    `home`      VARCHAR(100) NOT NULL,
    `upgrades`  VARCHAR(100) NOT NULL
);