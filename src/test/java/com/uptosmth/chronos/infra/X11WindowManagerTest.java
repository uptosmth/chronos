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

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uptosmth.chronos.domain.activity.window.ActiveWindow;

class X11WindowManagerTest {

    X11WindowManager windowManager;

    @BeforeEach
    public void setUp() {
        windowManager = new X11WindowManager();
    }

    @Test
    public void getsActiveWindowProp() {
        Optional<ActiveWindow> application = windowManager.getActiveWindow();

        assertTrue(application.isPresent());
    }

    @Test
    public void parsesX11WindowProp() throws IOException {
        Optional<ActiveWindow> application =
                windowManager.getActiveWindowFromLines(
                        Files.readAllLines(new File("src/test/resources/x11/idea.txt").toPath()));

        assertTrue(application.isPresent());

        assertEquals("jetbrains-idea-ce", application.get().getApplication());
        assertEquals("UNKNOWN", application.get().getCategory());
        assertEquals("chronos – X11ActiveWindowTest.java", application.get().getWindowTitle());
    }

    @Test
    public void returnsIdleTime() {
        assertDoesNotThrow(() -> windowManager.getIdleMilli());
    }

    @Test
    public void returnsAwayFlag() {
        assertDoesNotThrow(() -> windowManager.isAway());
    }
}
