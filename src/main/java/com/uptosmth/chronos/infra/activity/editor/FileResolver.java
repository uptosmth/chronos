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

package com.uptosmth.chronos.infra.activity.editor;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.javatuples.Pair;

import com.uptosmth.chronos.domain.activity.editor.EditorPayload;
import com.uptosmth.chronos.infra.Command;

class VcsInfo {

    public Optional<Pair<String, String>> get(Path file) {
        Path root = file.getParent();

        while (root != null) {
            if (root.resolve(".git").toFile().isDirectory()) {
                String projectName = getGitProjectName(root);
                String branch = getGitBranch(root);

                if (projectName != null && branch != null) {
                    return Optional.<Pair<String, String>>of(new Pair(projectName, branch));
                }
            }

            root = root.getParent();
        }

        return Optional.empty();
    }

    private String getGitProjectName(Path root) {
        List<String> lines =
                new Command(root).execLines(Arrays.asList("git", "rev-parse", "--show-toplevel"));

        if (lines.size() == 1) {
            String topLevel = lines.get(0);

            if (topLevel.startsWith("http") || topLevel.startsWith("git")) {
                return null;
            } else {
                return new File(topLevel).getName();
            }
        }

        return null;
    }

    private String getGitBranch(Path path) {
        Path root = path;
        while (root != null) {
            if (root.resolve(".git").toFile().isDirectory()) {
                List<String> lines =
                        new Command()
                                .execLines(
                                        Arrays.asList("git", "rev-parse", "--abbrev-ref", "HEAD"));

                if (lines.size() == 1) {
                    return lines.get(0);
                } else {
                    return null;
                }
            }

            root = root.getParent();
        }

        return null;
    }
}

public class FileResolver {
    public EditorPayload resolve(String file, String project, String branch) {
        Objects.requireNonNull(file);

        if (new File(file).isFile() && new File(file).canRead()) {
            Optional<Pair<String, String>> optionalVscInfo =
                    new VcsInfo().get(new File(file).toPath());

            if (optionalVscInfo.isPresent()) {
                project = optionalVscInfo.get().getValue0();
                branch = optionalVscInfo.get().getValue1();
            }

            if (project == null) {
                Path parent = new File(file).toPath().getParent();
                if (parent != null) {
                    project = parent.toAbsolutePath().getFileName().toString();
                }
            }
        }

        return new EditorPayload(file, project, branch);
    }
}
