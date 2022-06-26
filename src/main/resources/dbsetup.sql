CREATE TABLE IF NOT EXISTS name_colors
(
    uuid BINARY(16)           NOT NULL,
    pattern TINYTEXT          NOT NULL,
    PRIMARY KEY (uuid)
);
