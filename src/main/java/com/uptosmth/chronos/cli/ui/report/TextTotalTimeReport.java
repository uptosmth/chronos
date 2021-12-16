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

package com.uptosmth.chronos.cli.ui.report;

import java.time.LocalDate;

import javax.sql.DataSource;

import com.uptosmth.chronos.cli.ui.TextUi;
import com.uptosmth.chronos.report.TotalTimeReport;

public class TextTotalTimeReport {
    private final DataSource dataSource;

    public TextTotalTimeReport(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String render(LocalDate since, LocalDate until) {
        TotalTimeReport totalTimeReport = new TotalTimeReport(dataSource);

        StringBuilder sb = new StringBuilder();

        sb.append(TextUi.title("TOTAL TIME"));
        sb.append("\n");
        sb.append(TextUi.center(totalTimeReport.run(since, until).toString()));

        return sb.toString();
    }
}
