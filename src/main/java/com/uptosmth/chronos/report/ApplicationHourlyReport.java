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

import static com.uptosmth.chronos.db.Tables.*;
import static org.jooq.impl.DSL.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.javatuples.Pair;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class ApplicationHourlyReport {
    private final DataSource dataSource;

    public ApplicationHourlyReport(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<Integer, List<Pair<String, Integer>>> run() {
        try (Connection connection = this.dataSource.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.SQLITE);

            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);

            Result<Record3<Integer, String, BigDecimal>> result =
                    create.select(
                                    hour(ACTIVITY.CREATED_AT),
                                    WINDOW_ACTIVITY.APPLICATION,
                                    sum(ACTIVITY.ELAPSED_MILLI))
                            .from(ACTIVITY)
                            .join(WINDOW_ACTIVITY)
                            .on(WINDOW_ACTIVITY.ID.eq(ACTIVITY.ID))
                            .where(ACTIVITY.CREATED_AT.ge(today.atStartOfDay()))
                            .and(ACTIVITY.CREATED_AT.lt(tomorrow.atStartOfDay()))
                            .groupBy(DSL.field("1"), DSL.field("2"))
                            .orderBy(upper(WINDOW_ACTIVITY.APPLICATION).asc())
                            .fetch();

            Map<Integer, List<Pair<String, Integer>>> buckets = new HashMap<>();

            for (Record record : result) {
                Integer hour = record.getValue(0, Integer.class);
                String application = record.getValue(WINDOW_ACTIVITY.APPLICATION);
                Integer seconds = record.getValue(2, BigDecimal.class).intValue() / 1000;

                if (!buckets.containsKey(hour)) {
                    buckets.put(hour, new ArrayList<>());
                }

                buckets.get(hour).add(new Pair(application, seconds));
            }

            System.out.println(buckets);

            return buckets;
        } catch (SQLException e) {
            throw new RuntimeException("Error nextval", e);
        }
    }
}
