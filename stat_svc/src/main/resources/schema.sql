CREATE TABLE IF NOT EXISTS statistics
(
    statistical_data_id BIGINT AUTO_INCREMENT NOT NULL,
    app                 VARCHAR(255)          NOT NULL,
    uri                 VARCHAR(255)          NOT NULL,
    ip                  VARCHAR(255)          NOT NULL,
    timestamp           datetime              NOT NULL,
    CONSTRAINT pk_statistics PRIMARY KEY (statistical_data_id)
);