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

import static com.uptosmth.chronos.db.Tables.HEARTBEAT;

import java.sql.Connection;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class DatabaseUtils {
    private static final Logger log = LogManager.getLogger(DatabaseUtils.class);

    public static void processHeartbeats(Connection connection, List<UUID> heartbeats) {
        DSLContext context = DSL.using(connection, SQLDialect.SQLITE);

        Instant processedAt = Instant.now();

        for (UUID heartbeat : heartbeats) {
            int rowsUpdated =
                    context.update(HEARTBEAT)
                            .set(
                                    HEARTBEAT.PROCESSED_AT,
                                    LocalDateTime.ofInstant(processedAt, ZoneOffset.UTC))
                            .where(HEARTBEAT.UUID.eq(heartbeat.toString()))
                            .execute();

            if (rowsUpdated != 1) throw new RuntimeException("No heartbeat rows updated");
        }
    }
}
