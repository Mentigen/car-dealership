--liquibase formatted sql

--changeset mentigen:002-seed-users
INSERT INTO users (id, created_at, updated_at, removed, first_name, last_name, email, phone, password_hash, role)
VALUES
    ('a0000000-0000-0000-0000-000000000001', NOW(), NOW(), FALSE, 'Ivan', 'Petrov', 'manager@dealer.ru', '+79001234567', '$2a$10$dummyhashmanager', 'MANAGER'),
    ('a0000000-0000-0000-0000-000000000002', NOW(), NOW(), FALSE, 'Anna', 'Sidorova', 'customer@mail.ru', '+79007654321', '$2a$10$dummyhashcustomer', 'USER'),
    ('a0000000-0000-0000-0000-000000000003', NOW(), NOW(), FALSE, 'Admin', 'System', 'admin@dealer.ru', '+79009999999', '$2a$10$dummyhashadmin', 'ADMIN'),
    ('a0000000-0000-0000-0000-000000000004', NOW(), NOW(), FALSE, 'Warehouse', 'Admin', 'warehouse@dealer.ru', '+79008888888', '$2a$10$dummyhashwarehouse', 'WAREHOUSE_ADMIN');
