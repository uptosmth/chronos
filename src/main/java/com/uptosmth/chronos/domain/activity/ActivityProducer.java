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

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.uptosmth.chronos.domain.PayloadType;
import com.uptosmth.chronos.domain.heartbeat.HeartbeatAggregator;
import com.uptosmth.chronos.domain.heartbeat.HeartbeatRepository;

public class ActivityProducer<P> {
    private final ActivityRepository<P> activityRepository;
    private final HeartbeatRepository<P> heartbeatRepository;
    private final PayloadType payloadType;
    private final long samplingIntervalMilli;

    public ActivityProducer(
            ActivityRepository<P> activityRepository,
            HeartbeatRepository<P> heartbeatRepository,
            PayloadType payloadType,
            long samplingIntervalMilli) {
        this.activityRepository = activityRepository;
        this.heartbeatRepository = heartbeatRepository;
        this.payloadType = payloadType;
        this.samplingIntervalMilli = samplingIntervalMilli;
    }

    public int produce() {
        AtomicInteger produced = new AtomicInteger();

        HeartbeatAggregator<P> heartbeatAggregator =
                new HeartbeatAggregator<>(heartbeatRepository, payloadType, samplingIntervalMilli);

        heartbeatAggregator.aggregate(
                (aggregated) -> {
                    Optional<Activity<P>> lastActivity = activityRepository.loadLast(payloadType);

                    if (lastActivity.isPresent()
                            && lastActivity.get().payloadEquals(aggregated.getPayload())
                            && lastActivity
                                    .get()
                                    .isFinishedAfter(
                                            aggregated
                                                    .getCreatedAt()
                                                    .minusMillis(samplingIntervalMilli * 2))) {

                        lastActivity.ifPresent(
                                activity -> {
                                    activity.addElapsedMilli(aggregated.getElapsedMilli());
                                    activity.addHeartbeats(aggregated.getHeartbeats());

                                    activityRepository.store(activity);
                                });
                    } else {
                        Activity<P> activity =
                                Activity.<P>builder()
                                        .uuid(UUID.randomUUID())
                                        .type(payloadType)
                                        .startedAt(aggregated.getCreatedAt())
                                        .elapsedMilli(aggregated.getElapsedMilli())
                                        .heartbeats(aggregated.getHeartbeats())
                                        .payload(aggregated.getPayload())
                                        .build();

                        activityRepository.append(activity);
                    }

                    produced.getAndIncrement();
                });

        return produced.get();
    }
}
