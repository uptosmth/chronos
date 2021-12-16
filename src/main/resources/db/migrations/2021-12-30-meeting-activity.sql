--liquibase formatted sql
--changeset vti:71892d6e-6999-11ec-ba45-c39eb2058736

CREATE TABLE IF NOT EXISTS meeting_activity (
    id INTEGER NOT NULL PRIMARY KEY,
    application TEXT NOT NULL
);