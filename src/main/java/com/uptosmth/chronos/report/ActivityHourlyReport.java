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

package com.uptosmth.chronos.report;

import static com.uptosmth.chronos.db.Tables.ACTIVITY;
import static org.jooq.impl.DSL.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.sql.DataSource;

import org.javatuples.Pair;
import org.jooq.*;
import org.jooq.impl.DSL;

import com.uptosmth.chronos.domain.PayloadType;

public class ActivityHourlyReport {
    private final DataSource dataSource;

    public ActivityHourlyReport(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Pair<Integer, Integer>> run(
            PayloadType payloadType, LocalDate since, LocalDate until) {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.SQLITE);

            List<Pair<Integer, Integer>> buckets = new ArrayList<>();

            for (int hour : IntStream.range(0, 24).toArray()) {
                LocalDateTime startHour = since.atStartOfDay().plusHours(hour);
                LocalDateTime endHour = startHour.plusHours(1);

                Record2<LocalDateTime, LocalDateTime> record =
                        create.select(
                                        min(ACTIVITY.LOCAL_STARTED_AT),
                                        max(ACTIVITY.LOCAL_FINISHED_AT))
                                .from(ACTIVITY)
                                .where(ACTIVITY.LOCAL_STARTED_AT.lt(endHour))
                                .and(ACTIVITY.LOCAL_FINISHED_AT.gt(startHour))
                                .and(ACTIVITY.LOCAL_STARTED_AT.lt(until.atStartOfDay()))
                                .and(ACTIVITY.TYPE.eq(payloadType.toString()))
                                .fetchOne();

                if (record != null) {
                    LocalDateTime startedAt = record.getValue(0, LocalDateTime.class);
                    LocalDateTime finishedAt = record.getValue(1, LocalDateTime.class);

                    if (startedAt == null || finishedAt == null) {
                        buckets.add(new Pair(hour, 0));
                    } else {
                        if (startedAt.isBefore(startHour)) {
                            startedAt = startHour;
                        }

                        if (finishedAt.isAfter(endHour)) {
                            finishedAt = endHour;
                        }

                        Long elapsedSecond =
                                finishedAt.toEpochSecond(ZoneOffset.UTC)
                                        - startedAt.toEpochSecond(ZoneOffset.UTC);

                        buckets.add(new Pair(hour, elapsedSecond.intValue()));
                    }
                }
            }

            return buckets;
        } catch (SQLException e) {
            throw new RuntimeException("Error", e);
        }
    }
}
