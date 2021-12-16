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
import static com.uptosmth.chronos.db.Tables.WINDOW_ACTIVITY;
import static org.jooq.impl.DSL.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.javatuples.Pair;
import org.jooq.*;
import org.jooq.impl.DSL;

public class WindowTitleReport {
    private final DataSource dataSource;

    public WindowTitleReport(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Pair<String, Integer>> run(LocalDate since, LocalDate until) {
        try (Connection connection = this.dataSource.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.SQLITE);

            Result<Record2<String, BigDecimal>> result =
                    create.select(WINDOW_ACTIVITY.WINDOW_TITLE, sum(ACTIVITY.ELAPSED_MILLI))
                            .from(ACTIVITY)
                            .join(WINDOW_ACTIVITY)
                            .on(WINDOW_ACTIVITY.ID.eq(ACTIVITY.ID))
                            .where(ACTIVITY.CREATED_AT.ge(since.atStartOfDay()))
                            .and(ACTIVITY.CREATED_AT.lt(until.atStartOfDay()))
                            .groupBy(upper(WINDOW_ACTIVITY.WINDOW_TITLE))
                            .orderBy(DSL.field("2").desc())
                            .limit(10)
                            .fetch();

            List<Pair<String, Integer>> windowTitles = new ArrayList<>();

            for (Record record : result) {
                String windowTitle = record.getValue(WINDOW_ACTIVITY.WINDOW_TITLE);
                Integer seconds = record.getValue(1, BigDecimal.class).intValue() / 1000;

                windowTitles.add(new Pair(windowTitle, seconds));
            }

            return windowTitles;
        } catch (SQLException e) {
            throw new RuntimeException("Error", e);
        }
    }
}
