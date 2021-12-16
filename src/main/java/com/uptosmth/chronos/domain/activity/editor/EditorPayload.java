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

package com.uptosmth.chronos.domain.activity.editor;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EditorPayload {
    private String file;
    private String project;
    private String branch;

    @JsonCreator
    public EditorPayload(
            @JsonProperty(value = "file", required = true) String file,
            @JsonProperty(value = "project") String project,
            @JsonProperty(value = "branch") String branch) {
        Objects.requireNonNull(file, "file");

        this.file = file;
        this.project = project;
        this.branch = branch;
    }

    public String getFile() {
        return file;
    }

    public String getProject() {
        return project;
    }

    public String getBranch() {
        return branch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditorPayload that = (EditorPayload) o;
        return Objects.equals(file, that.file)
                && Objects.equals(project, that.project)
                && Objects.equals(branch, that.branch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, project, branch);
    }
}
