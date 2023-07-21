CREATE TABLE IF NOT EXISTS statistics
(
    statistical_data_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app                 VARCHAR(255)                            NOT NULL,
    uri                 VARCHAR(1023)                           NOT NULL,
    ip                  VARCHAR(31)                             NOT NULL,
    timestamp           TIMESTAMP                               NOT NULL,
    CONSTRAINT pk_statistics PRIMARY KEY (statistical_data_id)
);