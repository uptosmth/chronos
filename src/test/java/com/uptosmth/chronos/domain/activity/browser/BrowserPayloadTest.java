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

package com.uptosmth.chronos.domain.activity.browser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class BrowserPayloadTest {

    @Test
    public void extractsDomain() {
        BrowserPayload browserPayload =
                new BrowserPayload("Strava", "https://strava.com/athletes/1234567890");

        assertNotNull(browserPayload);

        assertEquals("strava.com", browserPayload.getUrlDomain());
    }

    @Test
    public void ignoresAbout() {
        BrowserPayload browserPayload =
                new BrowserPayload("Strava", "about:devtools-toolbox?id=foo&type=extension");

        assertNotNull(browserPayload);

        assertEquals("localhost", browserPayload.getUrlDomain());
    }

    @Test
    public void ignoresFile() {
        BrowserPayload browserPayload = new BrowserPayload("Strava", "file:///some/file.txt");

        assertNotNull(browserPayload);

        assertEquals("localhost", browserPayload.getUrlDomain());
    }
}
