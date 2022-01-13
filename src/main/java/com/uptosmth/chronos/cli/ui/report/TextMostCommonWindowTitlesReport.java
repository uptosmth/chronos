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
import java.util.List;

import javax.sql.DataSource;

import org.javatuples.Pair;

import com.uptosmth.chronos.cli.ui.TextUi;
import com.uptosmth.chronos.cli.ui.chart.DurationFormatter;
import com.uptosmth.chronos.report.WindowTitleReport;

public class TextMostCommonWindowTitlesReport {
    private final DataSource dataSource;

    public TextMostCommonWindowTitlesReport(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String render(LocalDate since, LocalDate until) {
        StringBuilder sb = new StringBuilder();

        sb.append(TextUi.title("MOST COMMON WINDOW TITLES"));
        sb.append("\n");

        WindowTitleReport windowTitleReport = new WindowTitleReport(dataSource);

        List<Pair<String, Integer>> windowTitleReportData = windowTitleReport.run(since, until);

        for (Pair<String, Integer> windowTitleStat : windowTitleReportData) {
            sb.append(
                    TextUi.keyValue(
                            windowTitleStat.getValue0(),
                            DurationFormatter.format(windowTitleStat.getValue1())));
            sb.append("\n");
        }

        return sb.toString();
    }
}
