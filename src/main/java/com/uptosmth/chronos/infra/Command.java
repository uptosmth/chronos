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

package com.uptosmth.chronos.infra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Command {
    private Path cwd = null;

    public Command() {}

    public Command(Path cwd) {
        this.cwd = cwd;
    }

    public List<String> execLines(List<String> command) {
        ProcessBuilder builder = new ProcessBuilder();

        Map<String, String> env = builder.environment();
        env.put("LANG", "en_US.UTF-8");
        env.put("LC_ALL", "en_US.UTF-8");

        if (cwd != null) {
            builder.directory(cwd.toFile());
        }

        builder.command(command);

        try {
            Process process = builder.start();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    process.getInputStream(), StandardCharsets.UTF_8));

            List<String> lines = new ArrayList<>();

            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            int exitCode = process.waitFor();

            return lines;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Command failed", e);
        }
    }
}
