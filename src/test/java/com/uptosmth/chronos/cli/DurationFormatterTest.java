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

package com.uptosmth.chronos.cli;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.uptosmth.chronos.cli.ui.chart.DurationFormatter;

class DurationFormatterTest {

    @Test
    void formatsDuration() {
        assertEquals(" 0m  1s", DurationFormatter.format(1));
        assertEquals(" 0m 25s", DurationFormatter.format(25));

        assertEquals(" 1m  0s", DurationFormatter.format(60));
        assertEquals(" 1m  5s", DurationFormatter.format(65));

        assertEquals(" 1h  0m", DurationFormatter.format(3600));
        assertEquals(" 1h  1m", DurationFormatter.format(3660));
        assertEquals(" 1h  1m", DurationFormatter.format(3661));

        assertEquals("24h  0m", DurationFormatter.format(24 * 3600));
        assertEquals("25h  1m", DurationFormatter.format(24 * 3600 + 3600 + 60));
        assertEquals("26h  0m", DurationFormatter.format(24 * 3600 + 2 * 3600));
    }
}
