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

package com.uptosmth.chronos.cli.ui;

public class TextUi {
    public static int WIDTH = 72;

    public static String repeat(String character, int length) {
        return new String(new char[length]).replace("\0", character);
    }

    public static String line() {
        return repeat("-", WIDTH);
    }

    public static String line(String character) {
        return repeat(character, WIDTH);
    }

    public static String center(String text) {
        int middle = text.length() / 2;

        StringBuilder sb = new StringBuilder();

        sb.append(repeat(" ", WIDTH / 2 - middle));
        sb.append(text);

        return sb.toString();
    }

    public static String header(String title) {
        return String.join("\n", line("="), center(title), line("=")) + "\n";
    }

    public static String title(String title) {
        return String.join("\n", line(), center(title), line()) + "\n";
    }

    public static String keyValue(String key, String value) {
        if (key == null) key = "<empty>";

        if (value == null) value = "<empty>";

        key = truncate(key, WIDTH - value.length() - 3);

        int justifyWidth = WIDTH - key.length() - value.length() - 2;

        StringBuilder sb = new StringBuilder();

        sb.append(key);
        sb.append(" ");
        sb.append(repeat(".", justifyWidth));
        sb.append(" ");
        sb.append(value);

        return sb.toString();
    }

    public static String truncate(String data, int maxLength) {
        if (data.length() > maxLength) {
            return data.substring(0, maxLength);
        }

        return data;
    }
}
