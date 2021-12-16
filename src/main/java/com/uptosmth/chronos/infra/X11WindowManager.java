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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.uptosmth.chronos.domain.WindowManager;
import com.uptosmth.chronos.domain.activity.window.ActiveWindow;

public class X11WindowManager implements WindowManager {

    private static final String ACTIVE_WINDOW = "_NET_ACTIVE_WINDOW";
    private static final String WM_CLASS = "WM_CLASS";
    private static final String WM_NAME = "WM_NAME";

    static {
        System.loadLibrary("x11windowmanager");
    }

    @Override
    public Optional<ActiveWindow> getActiveWindow() {
        Optional<String> optionalId = getActiveWindowId();

        if (!optionalId.isPresent()) {
            return Optional.empty();
        }

        List<String> output =
                new Command()
                        .execLines(
                                Arrays.asList("xprop", "-id", optionalId.get(), WM_CLASS, WM_NAME));

        return getActiveWindowFromLines(output);
    }

    @Override
    public boolean isWindowPresent(String title) {
        return new Command()
                .execLines(Arrays.asList("xwininfo", "-root", "-tree")).stream()
                        .anyMatch((l) -> l.contains(title));
    }

    @Override
    public native long getIdleMilli();

    @Override
    public native boolean isAway();

    public Optional<ActiveWindow> getActiveWindowFromLines(List<String> output) {
        String category = "UNKNOWN";
        String application = null;
        String windowTitle = null;

        for (String line : output) {
            if (line.startsWith(WM_CLASS)) {
                application = matchAndExtract(line, "WM_CLASS\\(STRING\\) = \".*?\", \"(.*?)\"");
            } else if (line.startsWith(WM_NAME)) {
                windowTitle = matchAndExtract(line, "WM_NAME\\(.*?\\) = \"(.*)\"");
            }
        }

        if (category != null
                && !category.isEmpty()
                && application != null
                && !application.isEmpty()
                && windowTitle != null
                && !windowTitle.isEmpty()) {
            return Optional.of(
                    ActiveWindow.builder()
                            .category(category)
                            .application(application)
                            .windowTitle(windowTitle)
                            .build());
        }

        return Optional.empty();
    }

    private Optional<String> getActiveWindowId() {
        List<String> output =
                new Command().execLines(Arrays.asList("xprop", "-root", ACTIVE_WINDOW));

        if (output.size() == 0) {
            return Optional.empty();
        }

        return Optional.of(matchAndExtract(output.get(0), "id # (.*?),"));
    }

    private String matchAndExtract(String data, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }
}
