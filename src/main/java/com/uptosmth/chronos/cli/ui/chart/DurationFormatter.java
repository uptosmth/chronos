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

package com.uptosmth.chronos.cli.ui.chart;

public class DurationFormatter {
    private static final int MINUTE = 60;
    private static final int HOUR = MINUTE * 60;
    private static final int DAY = HOUR * 24;

    private DurationFormatter() {}

    public static String format(long seconds) {
        if (seconds < MINUTE) {
            return String.format(" 0m %2ss", seconds);
        }

        if (seconds < HOUR) {
            // boolean hasSeconds = seconds % MINUTE == 0 ? false : true;

            // if (hasSeconds) {
            return String.format("%2sm %2ss", seconds / MINUTE, seconds % MINUTE);
            // } else {
            //    return String.format("%2sm", seconds / MINUTE);
            // }
        }

        boolean hasMinutes = seconds % HOUR != 0;

        // if (hasMinutes) {
        return String.format("%2sh %2sm", seconds / HOUR, (seconds / MINUTE) % MINUTE);
        // } else {
        //    return String.format("%2sh", seconds / HOUR);
        // }
    }

    public static String formatCompact(long seconds) {
        if (seconds < MINUTE) {
            return String.format("%ss", seconds);
        }

        if (seconds < HOUR) {
            boolean hasSeconds = seconds % MINUTE != 0;

            if (hasSeconds) {
                return String.format("%sm %ss", seconds / MINUTE, seconds % MINUTE);
            } else {
                return String.format("%sm", seconds / MINUTE);
            }
        }

        boolean hasMinutes = seconds % HOUR != 0;

        if (hasMinutes) {
            return String.format("%sh %sm", seconds / HOUR, (seconds / MINUTE) % MINUTE);
        } else {
            return String.format("%sh", seconds / HOUR);
        }
    }
}
