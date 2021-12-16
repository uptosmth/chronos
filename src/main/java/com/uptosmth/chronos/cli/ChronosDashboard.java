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

package com.uptosmth.chronos.cli;

import java.time.LocalDate;
import java.util.concurrent.Callable;

import javax.sql.DataSource;

import picocli.CommandLine;

import com.uptosmth.chronos.App;
import com.uptosmth.chronos.cli.ui.TextUi;
import com.uptosmth.chronos.cli.ui.report.*;
import com.uptosmth.chronos.cli.util.DateReference;
import com.uptosmth.chronos.infra.DataSourceBuilder;

@CommandLine.Command(
        name = "dashboard",
        mixinStandardHelpOptions = true,
        sortOptions = false,
        aliases = {"dash"},
        description = "Display dashboard")
public class ChronosDashboard implements Callable<Integer> {

    @CommandLine.ParentCommand private App parent;

    @CommandLine.Option(
            names = "--since",
            paramLabel = "today",
            description = "Start from date (inclusive)")
    String sinceArg = "today";

    @CommandLine.Option(
            names = "--until",
            paramLabel = "tomorrow",
            description = "Stop at date (not inclusive)")
    String untilArg = "tomorrow";

    @Override
    public Integer call() {
        DataSource dataSource = new DataSourceBuilder(parent.dbArg.toPath()).getDataSource();

        LocalDate now = LocalDate.now();

        LocalDate since = DateReference.parse(now, sinceArg);
        LocalDate until = DateReference.parse(now, untilArg);

        System.out.println(
                TextUi.header(String.format("%s -> %s", since.toString(), until.toString())));

        System.out.println(new TextTotalTimeReport(dataSource).render(since, until));
        System.out.println();

        System.out.println(new TextMostActiveHoursReport(dataSource).render(since, until));
        System.out.println();

        System.out.println(new TextMostUsedApplicationsReport(dataSource).render(since, until));
        System.out.println();

        System.out.println(new TextMostCommonWindowTitlesReport(dataSource).render(since, until));
        System.out.println();

        System.out.println(new TextMostVisitedWebsitesReport(dataSource).render(since, until));
        System.out.println();

        System.out.println(new TextMostEditingReport(dataSource).render(since, until));
        System.out.println();

        System.out.println(new TextMeetingsReport(dataSource).render(since, until));
        System.out.println();

        return 1;
    }
}
