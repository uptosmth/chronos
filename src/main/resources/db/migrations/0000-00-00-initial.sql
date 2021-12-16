--liquibase formatted sql
--changeset vti:942a1992-61c3-11ec-86d4-fbe0440e997b

CREATE TABLE IF NOT EXISTS heartbeat (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    uuid TEXT NOT NULL,
    delivered_at TIMESTAMP NOT NULL,
    processed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    local_created_at TIMESTAMP,
    type TEXT NOT NULL,
    data TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS activity (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    uuid TEXT NOT NULL UNIQUE,
    type TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    started_at TIMESTAMP NOT NULL,
    finished_at TIMESTAMP,
    local_started_at TIMESTAMP,
    local_finished_at TIMESTAMP,
    elapsed_milli BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS window_activity (
    id INTEGER NOT NULL PRIMARY KEY,
    category TEXT NOT NULL,
    application TEXT NOT NULL,
    window_title TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS browser_activity (
    id INTEGER NOT NULL PRIMARY KEY,
    title TEXT NOT NULL,
    url TEXT NOT NULL,
    url_domain TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS editor_activity (
    id INTEGER NOT NULL PRIMARY KEY,
    file TEXT NOT NULL,
    project TEXT,
    branch TEXT
);