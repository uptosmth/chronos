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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.uptosmth.chronos.TestFactory;
import com.uptosmth.chronos.domain.PayloadType;
import com.uptosmth.chronos.domain.activity.Activity;

public class ActivityTest {

    @Test
    public void buildsWindowActivity() {
        ActiveWindow activeWindow = TestFactory.buildActiveWindow();

        Activity activity =
                Activity.builder()
                        .uuid(UUID.randomUUID())
                        .type(PayloadType.WINDOW)
                        .startedAt(Instant.parse("2021-01-01T01:02:03.100Z"))
                        .elapsedMilli(1000)
                        .payload(
                                new WindowPayload(
                                        activeWindow.getCategory(),
                                        activeWindow.getApplication(),
                                        activeWindow.getWindowTitle()))
                        .build();

        assertNotNull(activity);
    }

    @Test
    public void addsElapsedMilli() {
        Activity activity = TestFactory.windowActivityBuilder().elapsedMilli(0).build();

        activity.addElapsedMilli(1000);
        activity.addElapsedMilli(2000);

        assertEquals(3000, activity.getElapsedMilli());
    }

    @Test
    public void comparesAttributes() {
        WindowPayload payload = new WindowPayload("CATEGORY", "APPLICATION", "TITLE");

        WindowPayload otherPayload1 = new WindowPayload("OTHER_CATEGORY", "APPLICATION", "TITLE");
        WindowPayload otherPayload2 = new WindowPayload("CATEGORY", "OTHER_APPLICATION", "TITLE");
        WindowPayload otherPayload3 = new WindowPayload("CATEGORY", "APPLICATION", "OTHER_TITLE");

        Activity activity =
                TestFactory.windowActivityBuilder()
                        .payload(new WindowPayload("CATEGORY", "APPLICATION", "TITLE"))
                        .build();

        assertTrue(activity.payloadEquals(payload));

        assertFalse(activity.payloadEquals(otherPayload1));
        assertFalse(activity.payloadEquals(otherPayload2));
        assertFalse(activity.payloadEquals(otherPayload3));
    }

    @Test
    public void checksIfFinishedBefore() {
        Activity activity =
                TestFactory.windowActivityBuilder()
                        .startedAt(Instant.parse("2021-01-01T10:00:00.000Z"))
                        .elapsedMilli(1000)
                        .build();

        assertFalse(activity.isFinishedAfter(Instant.now()));

        assertTrue(activity.isFinishedAfter(Instant.parse("2021-01-01T09:00:00.000Z")));
    }
}
