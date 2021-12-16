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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.uptosmth.chronos.TestFactory;
import com.uptosmth.chronos.domain.PayloadType;
import com.uptosmth.chronos.domain.activity.window.WindowPayload;

class HeartbeatAggregatorTest {
    private final long samplingIntervalMilli = 1_000;

    WindowPayload payload1 = new WindowPayload("UNKNOWN", "Firefox", "Chronos");

    WindowPayload payload2 = new WindowPayload("UNKNOWN", "Thunderbird", "Message");

    @Test
    void doesNothingWhenNotEnoughData() {
        List<Heartbeat<WindowPayload>> heartbeats = new ArrayList<>();
        HeartbeatRepository<WindowPayload> heartbeatRepository =
                new MemoryHeartbeatRepository<>(heartbeats);

        heartbeats.add(
                TestFactory.windowHeartbeatBuilder()
                        .createdAt(Instant.now())
                        .payload(payload1)
                        .build());

        HeartbeatAggregator<WindowPayload> aggregator =
                new HeartbeatAggregator<>(
                        heartbeatRepository, PayloadType.WINDOW, samplingIntervalMilli);

        List<AggregatedHeartbeat<WindowPayload>> aggregated = new ArrayList<>();

        aggregator.aggregate(aggregated::add);

        assertEquals(0, aggregated.size());
    }

    @Test
    void closesActivityWhenEnoughTimePassed() {
        List<Heartbeat<WindowPayload>> heartbeats = new ArrayList<>();
        HeartbeatRepository<WindowPayload> heartbeatRepository =
                new MemoryHeartbeatRepository<>(heartbeats);

        heartbeats.add(
                TestFactory.windowHeartbeatBuilder()
                        .createdAt(Instant.parse("2021-01-01T10:00:00.000Z"))
                        .payload(payload1)
                        .build());

        HeartbeatAggregator<WindowPayload> aggregator =
                new HeartbeatAggregator<>(
                        heartbeatRepository, PayloadType.WINDOW, samplingIntervalMilli);

        List<AggregatedHeartbeat<WindowPayload>> aggregated = new ArrayList<>();

        aggregator.aggregate(aggregated::add);

        assertEquals(1, aggregated.size());
        assertEquals(1_000, aggregated.get(0).getElapsedMilli());
    }

    @Test
    void gluesSameApplication() {
        List<Heartbeat<WindowPayload>> heartbeats = new ArrayList<>();
        HeartbeatRepository<WindowPayload> heartbeatRepository =
                new MemoryHeartbeatRepository<>(heartbeats);

        heartbeats.add(
                TestFactory.windowHeartbeatBuilder()
                        .createdAt(Instant.now().minusMillis(samplingIntervalMilli * 2))
                        .payload(payload1)
                        .build());
        heartbeats.add(
                TestFactory.windowHeartbeatBuilder()
                        .createdAt(Instant.now().minusMillis(samplingIntervalMilli))
                        .payload(payload1)
                        .build());
        heartbeats.add(
                TestFactory.windowHeartbeatBuilder()
                        .createdAt(Instant.now())
                        .payload(payload1)
                        .build());

        HeartbeatAggregator<WindowPayload> aggregator =
                new HeartbeatAggregator<>(
                        heartbeatRepository, PayloadType.WINDOW, samplingIntervalMilli);

        List<AggregatedHeartbeat<WindowPayload>> aggregated = new ArrayList<>();

        aggregator.aggregate(aggregated::add);

        assertEquals(1, aggregated.size());
        assertEquals(2000, aggregated.get(0).getElapsedMilli());
        assertEquals(2, aggregated.get(0).getHeartbeats().size());
    }

    @Test
    void breaksOnDifferentApplication() {
        List<Heartbeat<WindowPayload>> heartbeats = new ArrayList<>();
        HeartbeatRepository<WindowPayload> heartbeatRepository =
                new MemoryHeartbeatRepository<>(heartbeats);

        heartbeats.add(
                TestFactory.windowHeartbeatBuilder()
                        .createdAt(Instant.now().minusMillis(3000))
                        .payload(payload1)
                        .build());
        heartbeats.add(
                TestFactory.windowHeartbeatBuilder()
                        .createdAt(Instant.now().minusMillis(2000))
                        .payload(payload1)
                        .build());
        heartbeats.add(
                TestFactory.windowHeartbeatBuilder()
                        .createdAt(Instant.now().minusMillis(1000))
                        .payload(payload2)
                        .build());
        heartbeats.add(
                TestFactory.windowHeartbeatBuilder()
                        .createdAt(Instant.now())
                        .payload(payload2)
                        .build());

        HeartbeatAggregator<WindowPayload> aggregator =
                new HeartbeatAggregator<>(
                        heartbeatRepository, PayloadType.WINDOW, samplingIntervalMilli);

        List<AggregatedHeartbeat<WindowPayload>> aggregated = new ArrayList<>();

        aggregator.aggregate(aggregated::add);

        assertEquals(2, aggregated.size());
        assertTrue(aggregated.get(0).getElapsedMilli() >= 2000);
        assertEquals(2, aggregated.get(0).getHeartbeats().size());
        assertEquals(1000, aggregated.get(1).getElapsedMilli());
        assertEquals(1, aggregated.get(1).getHeartbeats().size());
    }

    @Test
    void neutralizeLongElapsed() {
        List<Heartbeat<WindowPayload>> heartbeats = new ArrayList<>();
        HeartbeatRepository<WindowPayload> heartbeatRepository =
                new MemoryHeartbeatRepository<>(heartbeats);

        heartbeats.add(
                TestFactory.windowHeartbeatBuilder()
                        .createdAt(Instant.now().minusMillis(12_000))
                        .payload(payload1)
                        .build());
        heartbeats.add(
                TestFactory.windowHeartbeatBuilder()
                        .createdAt(Instant.now().minusMillis(11_000))
                        .payload(payload1)
                        .build());
        heartbeats.add(
                TestFactory.windowHeartbeatBuilder()
                        .createdAt(Instant.now().minusMillis(10_000))
                        .payload(payload1)
                        .build());
        heartbeats.add(
                TestFactory.windowHeartbeatBuilder()
                        .createdAt(Instant.now())
                        .payload(payload1)
                        .build());

        HeartbeatAggregator<WindowPayload> aggregator =
                new HeartbeatAggregator<>(
                        heartbeatRepository, PayloadType.WINDOW, samplingIntervalMilli);

        List<AggregatedHeartbeat<WindowPayload>> aggregated = new ArrayList<>();

        aggregator.aggregate(aggregated::add);

        assertEquals(1, aggregated.size());
        assertEquals(3000, aggregated.get(0).getElapsedMilli());
    }
}
