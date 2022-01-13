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
import static com.uptosmth.chronos.db.Tables.MEETING_ACTIVITY;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.upper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.javatuples.Pair;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class MeetingReport {
    private final DataSource dataSource;

    public MeetingReport(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Pair<String, Integer>> run(LocalDate since, LocalDate until) {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.SQLITE);

            Result<Record2<String, BigDecimal>> result =
                    create.select(MEETING_ACTIVITY.APPLICATION, sum(ACTIVITY.ELAPSED_MILLI))
                            .from(ACTIVITY)
                            .join(MEETING_ACTIVITY)
                            .on(MEETING_ACTIVITY.ID.eq(ACTIVITY.ID))
                            .where(ACTIVITY.CREATED_AT.ge(since.atStartOfDay()))
                            .and(ACTIVITY.CREATED_AT.lt(until.atStartOfDay()))
                            .groupBy(upper(MEETING_ACTIVITY.APPLICATION))
                            .orderBy(DSL.field("2").desc())
                            .limit(10)
                            .fetch();

            List<Pair<String, Integer>> meetings = new ArrayList<>();

            for (Record record : result) {
                String meeting = record.getValue(MEETING_ACTIVITY.APPLICATION);
                Integer seconds = record.getValue(1, BigDecimal.class).intValue() / 1000;

                meetings.add(new Pair(meeting, seconds));
            }

            return meetings;
        } catch (SQLException e) {
            throw new RuntimeException("Error", e);
        }
    }
}
