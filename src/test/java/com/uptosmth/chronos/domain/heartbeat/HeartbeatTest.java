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

package com.uptosmth.chronos.domain.heartbeat;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.uptosmth.chronos.TestFactory;

class HeartbeatTest {

    @Test
    void calculatesElapsedDiff() {
        Heartbeat oldHeartbeat =
                TestFactory.windowHeartbeatBuilder()
                        .createdAt(Instant.parse("2021-01-01T10:00:00.000Z"))
                        .build();
        Heartbeat newHeartbeat =
                TestFactory.windowHeartbeatBuilder()
                        .createdAt(Instant.parse("2021-01-01T10:00:01.000Z"))
                        .build();

        assertEquals(1000, newHeartbeat.elapsedFrom(oldHeartbeat));
    }

    @Test
    void throwsWhenOtherHeartbeatIsNewer() {
        Heartbeat oldHeartbeat =
                TestFactory.windowHeartbeatBuilder()
                        .createdAt(Instant.parse("2021-01-01T10:00:00.000Z"))
                        .build();
        Heartbeat newHeartbeat =
                TestFactory.windowHeartbeatBuilder()
                        .createdAt(Instant.parse("2021-01-01T10:00:01.000Z"))
                        .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    oldHeartbeat.elapsedFrom(newHeartbeat);
                });
    }
}
