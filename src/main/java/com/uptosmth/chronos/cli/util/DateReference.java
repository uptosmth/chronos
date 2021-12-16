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

package com.uptosmth.chronos.cli.util;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateReference {
    private static final Pattern agoPattern =
            Pattern.compile("^(\\d+)\\s+(day|week|month|year)s?\\s+ago$");
    private static final Pattern datePattern = Pattern.compile("^(\\d{4})-(\\d{2})-(\\d{2})$");

    public static LocalDate parse(LocalDate now, String reference) {
        if (reference == null) {
            return now;
        }

        if (reference.equals("today")) {
            return now;
        }

        if (reference.equals("yesterday")) {
            return now.minusDays(1);
        }

        if (reference.equals("tomorrow")) {
            return now.plusDays(1);
        }

        LocalDate ago = tryAgoPattern(now, reference);
        if (ago != null) return ago;

        LocalDate date = tryDatePattern(reference);
        if (date != null) return date;

        throw new RuntimeException("Can't parse date");
    }

    private static LocalDate tryAgoPattern(LocalDate now, String reference) {
        Matcher m = agoPattern.matcher(reference);

        if (m.find()) {
            int count = Integer.parseInt(m.group(1));
            String unit = m.group(2);

            switch (unit) {
                case "day":
                    return now.minusDays(count);
                case "week":
                    return now.minusWeeks(count);
                case "month":
                    return now.minusMonths(count);
                case "year":
                    return now.minusYears(count);
                default:
                    throw new RuntimeException(String.format("Can't parse ago pattern: %s", unit));
            }
        }

        return null;
    }

    private static LocalDate tryDatePattern(String reference) {
        Matcher m = datePattern.matcher(reference);

        if (m.find()) {
            int year = Integer.parseInt(m.group(1));
            int month = Integer.parseInt(m.group(2));
            int day = Integer.parseInt(m.group(3));

            return LocalDate.of(year, month, day);
        } else {
            return null;
        }
    }
}
