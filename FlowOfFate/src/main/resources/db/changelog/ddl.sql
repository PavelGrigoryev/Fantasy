--liquibase formatted sql

--changeset Grigoryev_Pavel:1
CREATE TABLE IF NOT EXISTS blog_model
(
    id      VARCHAR(100) PRIMARY KEY,
    author  VARCHAR(200) NOT NULL,
    title   VARCHAR(200) NOT NULL,
    content VARCHAR(200) NOT NULL
);
