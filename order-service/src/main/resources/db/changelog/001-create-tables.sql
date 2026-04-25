--liquibase formatted sql

--changeset mentigen:001-create-users
CREATE TABLE users (
    id              UUID PRIMARY KEY,
    created_at      TIMESTAMP WITH TIME ZONE,
    updated_at      TIMESTAMP WITH TIME ZONE,
    removed         BOOLEAN NOT NULL DEFAULT FALSE,
    first_name      VARCHAR(255) NOT NULL,
    last_name       VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE,
    phone           VARCHAR(255),
    password_hash   VARCHAR(255) NOT NULL,
    role            VARCHAR(50) NOT NULL
);

--changeset mentigen:001-create-orders
CREATE TABLE orders (
    id                      UUID PRIMARY KEY,
    created_at              TIMESTAMP WITH TIME ZONE,
    updated_at              TIMESTAMP WITH TIME ZONE,
    removed                 BOOLEAN NOT NULL DEFAULT FALSE,
    order_type              VARCHAR(31) NOT NULL,
    status                  VARCHAR(255) NOT NULL,
    client_id               UUID NOT NULL REFERENCES users(id),
    manager_id              UUID NOT NULL REFERENCES users(id),
    price                   NUMERIC(19, 2) NOT NULL,
    car_id                  UUID,
    car_configuration_id    UUID
);

--changeset mentigen:001-create-test-drive-requests
CREATE TABLE test_drive_requests (
    id              UUID PRIMARY KEY,
    created_at      TIMESTAMP WITH TIME ZONE,
    updated_at      TIMESTAMP WITH TIME ZONE,
    removed         BOOLEAN NOT NULL DEFAULT FALSE,
    client_id       UUID NOT NULL REFERENCES users(id),
    car_id          UUID NOT NULL,
    start_time      TIMESTAMP NOT NULL,
    status          VARCHAR(255) NOT NULL
);

--changeset mentigen:001-create-outbox-events
CREATE TABLE outbox_events (
    id                      UUID PRIMARY KEY,
    order_id                UUID NOT NULL UNIQUE,
    order_type              VARCHAR(31) NOT NULL,
    car_id                  UUID,
    car_configuration_id    UUID,
    trace_id                UUID NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    sent                    BOOLEAN NOT NULL DEFAULT FALSE,
    sent_at                 TIMESTAMP WITH TIME ZONE
);
