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

package com.uptosmth.chronos;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.uptosmth.chronos.infra.Command;

class CommandTest {

    @Test
    void executesCommand() {
        List<String> lines = new Command().execLines(Arrays.asList("echo", "hello"));

        assertEquals(lines, Arrays.asList("hello"));
    }

    @Test
    void executesCommandWithUtf() {
        List<String> lines = new Command().execLines(Arrays.asList("echo", "привет"));

        assertEquals(lines, Arrays.asList("привет"));
    }
}
