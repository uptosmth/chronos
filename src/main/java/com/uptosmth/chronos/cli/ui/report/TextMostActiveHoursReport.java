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
import com.uptosmth.chronos.cli.ui.chart.BarChart;
import com.uptosmth.chronos.domain.PayloadType;
import com.uptosmth.chronos.report.ActivityHourlyReport;

public class TextMostActiveHoursReport {
    private final DataSource dataSource;

    public TextMostActiveHoursReport(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String render(LocalDate since, LocalDate until) {
        StringBuilder sb = new StringBuilder();

        ActivityHourlyReport hourlyReport = new ActivityHourlyReport(dataSource);

        List<Pair<Integer, Integer>> hourlyReportData =
                hourlyReport.run(PayloadType.WINDOW, since, until);

        while (hourlyReportData.size() > 0 && hourlyReportData.get(0).getValue1().equals(0)) {
            hourlyReportData.remove(0);
        }

        while (hourlyReportData.size() > 0
                && hourlyReportData.get(hourlyReportData.size() - 1).getValue1().equals(0)) {
            hourlyReportData.remove(hourlyReportData.size() - 1);
        }

        BarChart chart = new BarChart();

        for (Pair<Integer, Integer> bucket : hourlyReportData) {
            chart.addData(bucket.getValue0(), bucket.getValue1());
        }

        sb.append(TextUi.title("ACTIVITY PER HOUR"));
        sb.append(chart.render(TextUi.WIDTH));

        return sb.toString();
    }
}
