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

--changeset mentigen:001-create-car-models
CREATE TABLE car_models (
    id              UUID PRIMARY KEY,
    created_at      TIMESTAMP WITH TIME ZONE,
    updated_at      TIMESTAMP WITH TIME ZONE,
    removed         BOOLEAN NOT NULL DEFAULT FALSE,
    brand           VARCHAR(255) NOT NULL,
    model_name      VARCHAR(255) NOT NULL,
    price           NUMERIC(19, 2) NOT NULL,
    engine_power    INTEGER NOT NULL,
    engine_volume   DOUBLE PRECISION NOT NULL,
    fuel_type       VARCHAR(50) NOT NULL,
    body            VARCHAR(50) NOT NULL,
    drive           VARCHAR(50) NOT NULL
);

--changeset mentigen:001-create-parts
CREATE TABLE parts (
    id                  UUID PRIMARY KEY,
    created_at          TIMESTAMP WITH TIME ZONE,
    updated_at          TIMESTAMP WITH TIME ZONE,
    removed             BOOLEAN NOT NULL DEFAULT FALSE,
    part_kind           VARCHAR(31) NOT NULL,
    type                VARCHAR(255) NOT NULL,
    price               NUMERIC(19, 2) NOT NULL,
    color               VARCHAR(255),
    transmission_type   VARCHAR(50)
);

--changeset mentigen:001-create-part-compatible-models
CREATE TABLE part_compatible_models (
    part_id         UUID NOT NULL REFERENCES parts(id),
    car_model_id    UUID NOT NULL REFERENCES car_models(id),
    PRIMARY KEY (part_id, car_model_id)
);

--changeset mentigen:001-create-car-configurations
CREATE TABLE car_configurations (
    id              UUID PRIMARY KEY,
    created_at      TIMESTAMP WITH TIME ZONE,
    updated_at      TIMESTAMP WITH TIME ZONE,
    removed         BOOLEAN NOT NULL DEFAULT FALSE,
    car_model_id    UUID NOT NULL REFERENCES car_models(id)
);

--changeset mentigen:001-create-car-configuration-parts
CREATE TABLE car_configuration_parts (
    car_configuration_id    UUID NOT NULL REFERENCES car_configurations(id),
    part_id                 UUID NOT NULL REFERENCES parts(id),
    PRIMARY KEY (car_configuration_id, part_id)
);

--changeset mentigen:001-create-cars
CREATE TABLE cars (
    id                          UUID PRIMARY KEY,
    created_at                  TIMESTAMP WITH TIME ZONE,
    updated_at                  TIMESTAMP WITH TIME ZONE,
    removed                     BOOLEAN NOT NULL DEFAULT FALSE,
    car_configuration_id        UUID NOT NULL REFERENCES car_configurations(id),
    available_for_test_drive    BOOLEAN NOT NULL DEFAULT FALSE
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
    car_id                  UUID REFERENCES cars(id),
    car_configuration_id    UUID REFERENCES car_configurations(id)
);

--changeset mentigen:001-create-test-drive-requests
CREATE TABLE test_drive_requests (
    id              UUID PRIMARY KEY,
    created_at      TIMESTAMP WITH TIME ZONE,
    updated_at      TIMESTAMP WITH TIME ZONE,
    removed         BOOLEAN NOT NULL DEFAULT FALSE,
    client_id       UUID NOT NULL REFERENCES users(id),
    car_id          UUID NOT NULL REFERENCES cars(id),
    start_time      TIMESTAMP NOT NULL,
    status          VARCHAR(255) NOT NULL
);
