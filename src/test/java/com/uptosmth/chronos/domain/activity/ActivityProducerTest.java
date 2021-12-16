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

package com.uptosmth.chronos.domain.activity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.uptosmth.chronos.TestFactory;
import com.uptosmth.chronos.domain.PayloadType;
import com.uptosmth.chronos.domain.activity.window.WindowPayload;
import com.uptosmth.chronos.domain.heartbeat.HeartbeatRepository;
import com.uptosmth.chronos.domain.heartbeat.MemoryHeartbeatRepository;

class ActivityProducerTest {

    private final long samplingIntervalMilli = 1_000;

    @Test
    void doesNothingWhenNoHeartbeats() {

        ActivityRepository<WindowPayload> activityRepository = new MemoryActivityRepository<>();
        HeartbeatRepository<WindowPayload> heartbeatRepository = new MemoryHeartbeatRepository<>();

        ActivityProducer<WindowPayload> producer =
                new ActivityProducer<>(
                        activityRepository,
                        heartbeatRepository,
                        PayloadType.WINDOW,
                        samplingIntervalMilli);

        int produced = producer.produce();

        assertEquals(0, produced);
    }

    @Test
    void producesAggregatedActivity() {

        ActivityRepository<WindowPayload> activityRepository = new MemoryActivityRepository<>();
        HeartbeatRepository<WindowPayload> heartbeatRepository = new MemoryHeartbeatRepository<>();

        heartbeatRepository.append(TestFactory.buildWindowHeartbeat());
        heartbeatRepository.append(TestFactory.buildWindowHeartbeat());
        heartbeatRepository.append(TestFactory.buildWindowHeartbeat());

        ActivityProducer<WindowPayload> producer =
                new ActivityProducer<>(
                        activityRepository,
                        heartbeatRepository,
                        PayloadType.WINDOW,
                        samplingIntervalMilli);

        int produced = producer.produce();

        assertEquals(1, produced);
    }

    @Test
    void producesAggregatedActivities() {
        ActivityRepository<WindowPayload> activityRepository = new MemoryActivityRepository<>();
        HeartbeatRepository<WindowPayload> heartbeatRepository = new MemoryHeartbeatRepository<>();

        WindowPayload otherPayload = TestFactory.windowPayload().withWindowTitle("Other title");

        heartbeatRepository.append(TestFactory.buildWindowHeartbeat());
        heartbeatRepository.append(
                TestFactory.windowHeartbeatBuilder().payload(otherPayload).build());
        heartbeatRepository.append(TestFactory.buildWindowHeartbeat());

        ActivityProducer<WindowPayload> producer =
                new ActivityProducer<>(
                        activityRepository,
                        heartbeatRepository,
                        PayloadType.WINDOW,
                        samplingIntervalMilli);

        int produced = producer.produce();

        assertEquals(2, produced);
    }
}
