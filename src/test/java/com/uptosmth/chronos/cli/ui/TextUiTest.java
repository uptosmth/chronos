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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TextUiTest {

    @Test
    public void truncates() {
        assertEquals("foo", TextUi.truncate("foo", 10));

        assertEquals("a very lon", TextUi.truncate("a very long line", 10));
    }

    @Test
    public void keyValue() {
        TextUi.WIDTH = 20;

        assertEquals("foo ............ bar", TextUi.keyValue("foo", "bar"));

        assertEquals("a very long li . bar", TextUi.keyValue("a very long line", "bar"));
    }
}
