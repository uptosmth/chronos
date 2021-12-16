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
import static org.jooq.impl.DSL.sum;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.uptosmth.chronos.domain.PayloadType;

public class TotalTimeReport {
    private final DataSource dataSource;

    public TotalTimeReport(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public LocalTime run(LocalDate since, LocalDate until) {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.SQLITE);

            Record1<BigDecimal> record =
                    create.select(sum(ACTIVITY.ELAPSED_MILLI))
                            .from(ACTIVITY)
                            .where(ACTIVITY.LOCAL_STARTED_AT.lt(until.atStartOfDay()))
                            .and(ACTIVITY.LOCAL_FINISHED_AT.gt(since.atStartOfDay()))
                            .and(ACTIVITY.TYPE.eq(PayloadType.WINDOW.toString()))
                            .fetchOne();

            Integer totalTime =
                    Optional.ofNullable(record.get(0, BigDecimal.class))
                            .map(v -> v.intValue() / 1000)
                            .orElse(0);

            return LocalTime.ofSecondOfDay(totalTime);
        } catch (SQLException e) {
            throw new RuntimeException("Error nextval", e);
        }
    }
}
