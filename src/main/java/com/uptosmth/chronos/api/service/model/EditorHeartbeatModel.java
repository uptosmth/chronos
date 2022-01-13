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

package com.uptosmth.chronos.api.service.model;

public class EditorHeartbeatModel {

    private Long timestamp;
    private String file;
    private String project;
    private String branch;

    public EditorHeartbeatModel() {}

    public Long getTimestamp() {
        return timestamp;
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
}
