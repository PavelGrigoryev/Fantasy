--liquibase formatted sql

--changeset Grigoryev_Pavel:2
INSERT INTO blog_model (id, author, title, content)
VALUES ('1', 'George', 'Best', 'Bad'),
       ('2', 'Steve', 'Dark', 'Sour'),
       ('3', 'Shown', 'Jolly', 'Stone');
