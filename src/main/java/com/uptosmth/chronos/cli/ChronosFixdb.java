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

import java.util.concurrent.Callable;

import picocli.CommandLine;

import com.uptosmth.chronos.App;
import com.uptosmth.chronos.infra.DataSourceBuilder;

@CommandLine.Command(
        name = "fixdb",
        mixinStandardHelpOptions = true,
        sortOptions = false,
        description = "Fix database")
public class ChronosFixdb implements Callable<Integer> {

    @CommandLine.ParentCommand private App parent;

    @Override
    public Integer call() {
        new DataSourceBuilder(parent.dbArg.toPath()).releaseLocks();

        return 1;
    }
}
