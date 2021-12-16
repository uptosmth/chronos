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

package com.uptosmth.chronos.domain.activity.window;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WindowPayload {
    private String category;
    private String application;
    private String windowTitle;

    public WindowPayload(
            @JsonProperty(value = "category", required = true) String category,
            @JsonProperty(value = "application", required = true) String application,
            @JsonProperty(value = "windowTitle", required = true) String windowTitle) {
        this.category = Objects.requireNonNull(category, "category");
        this.application = Objects.requireNonNull(application, "application");
        this.windowTitle = Objects.requireNonNull(windowTitle, "windowTitle");
    }

    public String getCategory() {
        return category;
    }

    public String getApplication() {
        return application;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public WindowPayload withWindowTitle(String newTitle) {
        return new WindowPayload(category, application, newTitle);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WindowPayload that = (WindowPayload) o;
        return Objects.equals(category, that.category)
                && Objects.equals(application, that.application)
                && Objects.equals(windowTitle, that.windowTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, application, windowTitle);
    }
}
