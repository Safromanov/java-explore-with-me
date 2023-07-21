CREATE TABLE locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat FLOAT                                   NOT NULL,
    lon FLOAT                                   NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS category
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255),
    email VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    initiator_id       BIGINT REFERENCES users (id) ON DELETE CASCADE,
    annotation         VARCHAR(255),
    category_id        BIGINT REFERENCES category (id),
    description        VARCHAR(255),
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    location_id        BIGINT REFERENCES locations (id),
    paid               BOOLEAN,
    participant_limit  INTEGER                                 NOT NULL,
    request_moderation BOOLEAN,
    title              VARCHAR(255),
    state              INTEGER,
    CONSTRAINT pk_events PRIMARY KEY (id)
);

CREATE TABLE event_requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    requester_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    event_id     BIGINT REFERENCES events (id) ON DELETE CASCADE,
    created      TIMESTAMP WITHOUT TIME ZONE,
    status       INTEGER,
    CONSTRAINT pk_eventrequests PRIMARY KEY (id)
);