CREATE TABLE IF NOT EXISTS autosell_users (
    uuid CHAR(36) NOT NULL,
    autosell_preference BOOLEAN,
    autopickup_preference BOOLEAN,
    PRIMARY KEY (uuid)
);