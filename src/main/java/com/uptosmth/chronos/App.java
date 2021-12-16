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

package com.uptosmth.chronos;

import java.io.File;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import com.uptosmth.chronos.cli.ChronosDashboard;
import com.uptosmth.chronos.cli.ChronosFixdb;
import com.uptosmth.chronos.cli.ChronosRecord;

@Command(
        name = "chronos",
        mixinStandardHelpOptions = true,
        sortOptions = false,
        version = "chronos 1.0",
        description = "Spies on your by recording what application are being used and for how long",
        subcommands = {
            CommandLine.HelpCommand.class,
            ChronosDashboard.class,
            ChronosRecord.class,
            ChronosFixdb.class
        })
public class App {

    @CommandLine.Option(
            names = "--db",
            order = -10,
            paramLabel = "./chronos.db",
            description = "Path to the database",
            defaultValue = "./chronos.db",
            scope = CommandLine.ScopeType.INHERIT)
    public File dbArg = new File("./chronos.db");

    @CommandLine.Option(
            names = "-v",
            scope = CommandLine.ScopeType.INHERIT,
            description = "Enable verbose mode")
    public void setVerbose(boolean[] verbose) {
        Configurator.setRootLevel(verbose.length > 1 ? Level.DEBUG : Level.INFO);
    }

    public static void main(String... args) {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");

        int exitCode = new CommandLine(new App()).execute(args);

        System.exit(exitCode);
    }
}
