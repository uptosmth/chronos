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

import org.junit.jupiter.api.Test;

import com.uptosmth.chronos.TestFactory;

class AggregatedHeartbeatTest {

    @Test
    void aggregatesHeartbeats() {
        Heartbeat heartbeat1 = TestFactory.buildWindowHeartbeat();
        Heartbeat heartbeat2 = TestFactory.buildWindowHeartbeat();
        Heartbeat heartbeat3 = TestFactory.buildWindowHeartbeat();

        AggregatedHeartbeat aggregatedHeartbeat = new AggregatedHeartbeat(heartbeat1, 1000);

        assertEquals(1000, aggregatedHeartbeat.getElapsedMilli());

        aggregatedHeartbeat.add(heartbeat2, 1000);

        assertEquals(2000, aggregatedHeartbeat.getElapsedMilli());

        aggregatedHeartbeat.add(heartbeat3, 2000);

        assertEquals(heartbeat1.getCreatedAt(), aggregatedHeartbeat.getCreatedAt());
        assertEquals(4000, aggregatedHeartbeat.getElapsedMilli());
        assertEquals(3, aggregatedHeartbeat.getHeartbeats().size());
    }
}
