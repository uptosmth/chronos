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

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.IntStream;

import javax.sql.DataSource;

import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.uptosmth.chronos.TestDatabase;
import com.uptosmth.chronos.TestFactory;
import com.uptosmth.chronos.domain.PayloadType;
import com.uptosmth.chronos.domain.activity.Activity;
import com.uptosmth.chronos.domain.activity.ActivityRepository;
import com.uptosmth.chronos.domain.activity.window.WindowPayload;
import com.uptosmth.chronos.infra.activity.SqliteActivityRepository;

class ActivityHourlyReportTest {
    private ActivityHourlyReport report;
    private ActivityRepository<WindowPayload> activityRepository;

    @TempDir private Path tempDir;

    @BeforeEach
    void setUp() {
        DataSource dataSource = TestDatabase.setupDatabase(tempDir);

        report = new ActivityHourlyReport(dataSource);
        activityRepository = new SqliteActivityRepository<WindowPayload>(dataSource);
    }

    @Test
    void handlesEmptyData() {
        List<Pair<Integer, Integer>> buckets =
                report.run(PayloadType.WINDOW, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 2));

        assertEquals(24, buckets.size());

        for (Integer hour : IntStream.range(0, 24).toArray()) {
            assertEquals(hour, buckets.get(hour).getValue0());
            assertEquals(0, buckets.get(hour).getValue1());
        }
    }

    @Test
    void aggregatesByHourWhenLessThanAnHour() {
        LocalDateTime localDateTime = LocalDateTime.parse("2022-01-01T10:00:00.000");

        Activity<WindowPayload> activity =
                TestFactory.windowActivityBuilder()
                        .startedAt(localDateTime.toInstant(ZoneOffset.UTC))
                        .elapsedMilli(1800 * 1000)
                        .build();

        activityRepository.append(activity);

        List<Pair<Integer, Integer>> buckets =
                report.run(PayloadType.WINDOW, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 2));

        assertEquals(0, buckets.get(10).getValue1());
        assertEquals(1800, buckets.get(11).getValue1());
        assertEquals(0, buckets.get(12).getValue1());
    }

    @Test
    void aggregatesByHourWhenSpansWholeHour() {
        LocalDateTime localDateTime = LocalDateTime.parse("2022-01-01T10:00:00.000");

        Activity<WindowPayload> activity =
                TestFactory.windowActivityBuilder()
                        .startedAt(localDateTime.toInstant(ZoneOffset.UTC))
                        .elapsedMilli(3600 * 1000)
                        .build();

        activityRepository.append(activity);

        List<Pair<Integer, Integer>> buckets =
                report.run(PayloadType.WINDOW, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 2));

        assertEquals(0, buckets.get(10).getValue1());
        assertEquals(3600, buckets.get(11).getValue1());
        assertEquals(0, buckets.get(12).getValue1());
    }

    @Test
    void aggregatesByHourWhenSpansSeveralHours() {
        LocalDateTime localDateTime = LocalDateTime.parse("2022-01-01T10:15:00.000");

        Activity<WindowPayload> activity =
                TestFactory.windowActivityBuilder()
                        .startedAt(localDateTime.toInstant(ZoneOffset.UTC))
                        .elapsedMilli(2 * 3600 * 1000)
                        .build();

        activityRepository.append(activity);

        List<Pair<Integer, Integer>> buckets =
                report.run(PayloadType.WINDOW, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 2));

        assertEquals(0, buckets.get(10).getValue1());
        assertEquals(45 * 60, buckets.get(11).getValue1());
        assertEquals(60 * 60, buckets.get(12).getValue1());
        assertEquals(15 * 60, buckets.get(13).getValue1());
        assertEquals(0, buckets.get(14).getValue1());
    }
}
