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

public class ActiveWindow {

    private final String category;
    private final String application;
    private final String windowTitle;

    private ActiveWindow(Builder builder) {
        this.category = Objects.requireNonNull(builder.category, "category");
        this.application = Objects.requireNonNull(builder.application, "application");
        this.windowTitle = Objects.requireNonNull(builder.windowTitle);
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

    @Override
    public String toString() {
        return String.format(
                "ActiveWindow={category=%s" + ",application=%s}", category, application);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof ActiveWindow) {
            ActiveWindow activeWindow = (ActiveWindow) o;

            return this.getApplication().equals(activeWindow.getApplication());
        }

        return false;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String category;
        private String application;
        private String windowTitle;

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder application(String application) {
            this.application = application;
            return this;
        }

        public Builder windowTitle(String windowTitle) {
            this.windowTitle = windowTitle;
            return this;
        }

        public ActiveWindow build() {
            return new ActiveWindow(this);
        }
    }
}
