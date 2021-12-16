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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ActiveWindowTest {

    @Test
    public void equality() {

        ActiveWindow application1 =
                ActiveWindow.builder()
                        .category("UNKNOWN")
                        .application("firefox")
                        .windowTitle("Mozilla")
                        .build();

        ActiveWindow application2 =
                ActiveWindow.builder()
                        .category("UNKNOWN")
                        .application("firefox")
                        .windowTitle("Mozilla")
                        .build();

        ActiveWindow application3 =
                ActiveWindow.builder()
                        .category("UNKNOWN")
                        .application("chrom")
                        .windowTitle("Mozilla")
                        .build();

        assertTrue(application1.equals(application1));
        assertTrue(application1.equals(application2));
        assertTrue(application2.equals(application1));

        assertFalse(application1.equals(application3));
        assertFalse(application2.equals(application3));
        assertFalse(application3.equals(application1));
    }
}
