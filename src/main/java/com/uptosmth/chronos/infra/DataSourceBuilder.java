/*
 * Copyright (C) 2021 Viacheslav Tykhanovskyi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.uptosmth.chronos.infra;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.exception.LockException;
import liquibase.resource.ClassLoaderResourceAccessor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sqlite.SQLiteDataSource;

public class DataSourceBuilder {
    private static final Logger log = LogManager.getLogger(DataSourceBuilder.class);

    private final Path path;
    private DataSource dataSource = null;

    public DataSourceBuilder(Path path) {
        this.path = path;
    }

    public DataSource getDataSource() {
        if (this.dataSource == null) {
            SQLiteDataSource dataSource = new SQLiteDataSource();
            dataSource.setUrl(String.format("jdbc:sqlite:%s", path));

            this.dataSource = dataSource;

            try (Connection connection = this.dataSource.getConnection()) {
                log.debug("Running database migrations...");

                Database database =
                        DatabaseFactory.getInstance()
                                .findCorrectDatabaseImplementation(new JdbcConnection(connection));

                System.setProperty("liquibase.changeLogLockWaitTimeInMinutes", "1");
                System.setProperty("liquibase.changelogLockPollRate", "5");

                try (Liquibase liquibase =
                        new Liquibase(
                                "db/changelog.xml", new ClassLoaderResourceAccessor(), database)) {
                    liquibase.update("");
                }

                log.debug("Database is up-to-date");
            } catch (LockException e) {
                log.error("Database lock exception");

                throw new RuntimeException("Database error", e);
            } catch (SQLException | LiquibaseException e) {
                log.error("Database migrations failed", e);

                throw new RuntimeException("Database error", e);
            }
        }

        return this.dataSource;
    }

    public void releaseLocks() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(String.format("jdbc:sqlite:%s", path));

        try (Connection connection = dataSource.getConnection()) {
            log.debug("Fixing database...");

            Database database =
                    DatabaseFactory.getInstance()
                            .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            System.setProperty("liquibase.changeLogLockWaitTimeInMinutes", "0");
            System.setProperty("liquibase.changelogLockPollRate", "5");

            try (Liquibase liquibase =
                    new Liquibase(
                            "db/changelog.xml", new ClassLoaderResourceAccessor(), database)) {
                liquibase.forceReleaseLocks();
            }

            log.debug("Database is fixed");
        } catch (SQLException | LiquibaseException e) {
            log.error("Database fixing failed", e);

            throw new RuntimeException("Database error", e);
        }
    }
}
